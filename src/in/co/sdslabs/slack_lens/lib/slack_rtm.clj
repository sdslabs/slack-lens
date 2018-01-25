(ns in.co.sdslabs.slack-lens.lib.slack-rtm
  (:require [clj-slack.rtm :as rtm]
            [aleph.http :as http]
            [cheshire.core :as json]
            [manifold.stream :as s]
            [manifold.time :as t]
            [manifold.bus :as bus]
            [clojure.set :refer [rename-keys]]
            [clojure.core.async :refer [timeout <! go go-loop]]))

(def event-bus (bus/event-bus))

(def conn-reset-delay 60000)

(def rtm-conn (atom nil))

(defn- get-connection-map
  [url token]
  {:api-url url
   :token token})

(defn- test-check
  [options]
  (if (options :token)
  (do (try
        (:url (rtm/start options))
     (catch Exception e (str "caught exception: " (.getMessage e)))))
   (:api-url options)))

(defn- get-rtm-ws-url
  [url token]
  (-> (get-connection-map url token)
      test-check))

(defn- get-rtm-ws-connection
  [url]
  (prn url)
  (if-let [socket (try
                    @(http/websocket-client url)
                    (catch Exception e
                      (prn "error with socket")))]
    socket
    (prn "no socket found")))

(defn- thread-check
  [data func]
    (func {:replies (get-in data [:message :reply_count])
           :thread_ts
           (get-in data [:message :thread_ts])}))

(defn- publish-events
  [data func]
  (let [data-map (dissoc (json/parse-string data true) :source_team)]
    (println (data-map :subtype))
    (cond
          ;; for updating the reply_count for a thread
          (= (:subtype data-map) "message_replied") (thread-check data-map func)
          ;; if the message is deleted
          (= (:subtype data-map) "message_deleted") (as-> [:deleted_ts :subtype] $
                                  (select-keys data-map $)
                                  (rename-keys $ {:deleted_ts :thread_ts})
                                  (func $))
          (or (some (partial = (:subtype data-map)) ["file_share" "bot_message"])
               (not (data-map :subtype)))
      (do
      (bus/publish! event-bus (:type data-map) data)))))

(defn connect
  [url token]
  (-> (get-rtm-ws-url url token)
      get-rtm-ws-connection))

(defn subscribe
  [type func]
  (let [message-stream (bus/subscribe event-bus type)]
    (future (s/consume func message-stream))))

(defn- reset-conn
  [options func]
  (prn "running")
  (if-let [conn (connect (:url options) (:token options))]
    (do
      (s/consume #(publish-events % func) (->> conn (s/buffer 100)))
      (go
        (<! (timeout conn-reset-delay))
        (s/close! conn)))))


(defn start
  [options func]
  (go-loop [iteration-no 1]
         (reset-conn options func)
         (prn iteration-no)
         (<! (timeout conn-reset-delay))
         (recur (inc iteration-no))))
