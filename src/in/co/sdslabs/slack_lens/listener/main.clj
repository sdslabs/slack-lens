(ns in.co.sdslabs.slack-lens.listener.main
  (:require [in.co.sdslabs.slack-lens.lib.slack-rtm :as rtm]))

(defn- message-handler
  [& [args]]
  (println args))

(defn start
  [options]
  (let [rtm-conn (rtm/start options)]
    (rtm/subscribe "message" #(println %))
    rtm-conn))
