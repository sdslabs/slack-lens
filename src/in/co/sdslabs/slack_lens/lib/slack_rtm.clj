(ns in.co.sdslabs.slack-lens.lib.slack-rtm
  (:require [clj-slack.rtm :as rtm]
            [aleph.http :as http]
            [manifold.stream :as s]))

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

(defn connect
  [url token]
  (let [ws-url (get-rtm-ws-url url token)
        conn (get-rtm-ws-connection ws-url)]
    @(s/take! conn)
    conn))
