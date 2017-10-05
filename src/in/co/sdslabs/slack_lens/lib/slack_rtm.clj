(ns in.co.sdslabs.slack-lens.lib.slack-rtm
  (:require [clj-slack.rtm :as rtm]
            [aleph.http :as http]
            [cheshire.core :as json]
            [manifold.stream :as s]
            [manifold.time :as t]
            [manifold.bus :as bus]
            [clojure.core.async :refer [timeout <! go]]))

(def event-bus (bus/event-bus))

(def conn-reset-delay 30000)

(def rtm-conn (atom nil))

(defn- get-connection-map
  [url token]
  {:api-url url
   :token token})

(defn- test-check
  [options] 
  (if (options :token) (:url (rtm/start options)) (:api-url options)))

(defn- get-rtm-ws-url
  [url token]
  (-> (get-connection-map url token)
      test-check))

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
  (let [data-map (dissoc (json/parse-string data true) :source_team)]
    (bus/publish! event-bus (:type data-map) data)))

(defn connect
  [url token]
  (-> (get-rtm-ws-url url token)
       get-rtm-ws-connection))


(defn subscribe
  [type func]
  (let [message-stream (bus/subscribe event-bus type)]
    (future (s/consume func message-stream))))

(defn- reset-conn
  [options]
  (let [conn (connect (:url options) (:token options))]
    (s/consume publish-events (->> conn (s/buffer 100)))
    (go 
      (<! (timeout conn-reset-delay))
      (s/close! conn))))

(defn start
  [options]
  (t/every conn-reset-delay #(reset-conn options)))
