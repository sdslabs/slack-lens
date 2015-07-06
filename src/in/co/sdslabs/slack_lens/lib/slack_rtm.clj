(ns in.co.sdslabs.slack-lens.lib.slack-rtm
  (:require [clj-slack.rtm :as rtm]
            [aleph.http :as http]
            [cheshire.core :as json]
            [manifold.stream :as s]
            [manifold.bus :as bus]))

(def event-bus (bus/event-bus))

(defn- get-connection-map
  [url token]
  {:api-url url
   :token token})

(defn- get-rtm-ws-url
  [url token]
  (-> (get-connection-map url token)
      rtm/start
      :url))

(defn- get-rtm-ws-connection
  [url]
  (if-let [socket (try
                    @(http/websocket-client url)
                    (catch Exception e
                      nil))]
    socket
    nil))

(defn- publish-events
  [data]
  (let [data-map (json/parse-string data true)]
    (bus/publish! event-bus (:type data-map) data)))

(defn connect
  [url token]
  (let [ws-url (get-rtm-ws-url url token)
        conn (get-rtm-ws-connection ws-url)]
    conn))

(defn subscribe
  [type func]
  (let [message-stream (bus/subscribe event-bus type)]
    (future (s/consume func message-stream))))

(defn start
  [options]
  (let [conn (connect (:url options) (:token options))]
    (future (s/consume publish-events (->> conn (s/buffer 100))))))
