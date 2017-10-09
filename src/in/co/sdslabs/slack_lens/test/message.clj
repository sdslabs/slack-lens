(ns in.co.sdslabs.slack-lens.test.message
  (:use [org.httpkit.server])
  (:require [compojure.route :as route]
          [cheshire.core :as json]))

(def clients (atom {}))

(defn ws
  [req]
  (with-channel req con
    (swap! clients assoc con true)
          (doseq [client @clients mes (clojure.string/split-lines (slurp "messages"))]
            (send! (key client)  mes false) (prn mes))
    (on-close con (fn [status]
                    (swap! clients dissoc con)
                    (println con " disconnected. status: " status)))))

(defn -main [& args]
  (let [port (Integer/parseInt 
               (or (System/getenv "PORT") "8080"))]
    (run-server ws {:port port :join? false})))