(ns in.co.sdslabs.slack-lens.listener.run
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]
            [in.co.sdslabs.slack-lens.listener.main :as main]))

(def ^:private cli-options
  [["-t" "--token TOKEN" "Slack Bot Token"
    :default nil]
   ["-u" "--url URL" "URL for connection to Slack API"
    :default "https://slack.com/api"]
   ["-h" "--help HELP" "Help"]])

(defn- usage
  "Returns a summary of how to use listener"
  [options-summary]
  (->> ["Listener: Listens to events from slack indefinitely"
        ""
        "Usage: run [options]"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn- error-msg
  "Returns error messages concantanated"
  [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))


(defn- exit
  "Exit the script with given status and message"
  [status msg]
  (println msg)
  (System/exit status))


(defn -main
  "Main entry function for listener, takes -t as option for
   slack bot token

   Run it as

   lein run -m in.co.sdslabs.slack-lens.listener.run -t 'Token here'"
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
        (:help options) (exit 0 (usage summary))
        (not (or (contains? options :token) (contains? options :url))) 
          (exit 1 (str "You must pass bot token\n" (usage summary)))
        errors (exit 1 (error-msg errors)))
    (main/start options)))
