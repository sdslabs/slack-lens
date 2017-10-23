(ns in.co.sdslabs.slack-lens.listener.main
  (:require [in.co.sdslabs.slack-lens.lib
                                   [slack-rtm :as rtm]
                                   [es :as es]]
            [in.co.sdslabs.slack-lens.config.es :as es-config]
            [in.co.sdslabs.slack-lens.map-db :as map-db]
            [cheshire.core :as json]
            [clojure.java.io :as jio]))



(defn get-config
  []
  (-> (jio/resource "config.json")
       slurp
       (json/parse-string true)))

(def map1 (dissoc (get-config) :mapping1 :mapping3))
(def map2 (dissoc (get-config) :mapping1 :mapping2))
(def conn (es/connect-es (get-config)))

(defn- message-handler
  [& [args]]
  (println args))



(defn- user-map
  [response ]
  (conj (dissoc (es/search-user 
                    (get response :user) conn map1) :id)
        (dissoc response :user)))

(defn- channel-map
  [response]
  (conj {:channel (get (es/search-channel 
                   (get response :channel) conn map2) :name)}
        (dissoc response :channel)))

(defn- get-proper-response
  [response]
  (-> (json/parse-string response true)
      (dissoc :ts)
      (assoc :timestamp (System/currentTimeMillis))
      (user-map)
      (channel-map)))

(defn setup-elastic
  []
  (let [config (get-config)
        es-conn (es/connect-es config)]
    (def iexist (es/index_exist! es-conn (:index_name config)))
    (if 
      (es/ensure-index! 
        es-conn (:index_name config) es-config/settings es-config/mapping1)
      {:conn es-conn 
       :index-name (:index_name config)
       :mapping (:mapping config) 
       :index_exist iexist}
      nil)))

(defn start
  [options]
  (let [rtm-conn (rtm/start options)
        conn-options (setup-elastic)]
       (if (:index_exist conn-options)
         nil
         (map-db/main options (:conn conn-options) (get-config)))
       (rtm/subscribe "message" 
         (fn [x]
             (prn x)
             (es/elastic-feed (assoc options :response (get-proper-response x)
               ))))
    rtm-conn))
