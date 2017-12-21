(ns in.co.sdslabs.slack-lens.listener.main
  (:require [in.co.sdslabs.slack-lens.lib
                                   [slack-rtm :as rtm]
                                   [es :as es]]
            [in.co.sdslabs.slack-lens.config.es :as es-config]
            [in.co.sdslabs.slack-lens.map-db :as map-db]
            [cheshire.core :as json]
            [clojure.string :as str]
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
  (if (= (response :subtype) "bot_message")
    response
    (conj (dissoc (es/search-user
                    (get response :user) conn map1) :id)
          (dissoc response :user))))

(defn- channel-map
  [response]
  (conj {:channel (get (es/search-channel
                   (get response :channel) conn map2) :name)}
        (dissoc response :channel)))

(defn- ts
  [data keymap]
    (if (contains? data keymap)
    (assoc (dissoc data keymap)
           keymap (Double/valueOf (keymap data)))
    data))

(defn- ping-name
  [refname]
  (if refname
    (:name (es/search-user refname conn map1))
    refname))



(defn- tests[data](prn data) data)

(defn username-parse[data]
  (let [message (atom data)]
    (doseq [array (re-seq #"<([\d|\w|\:|/|\_|\.|\_|\-]+)>" data)]
      (swap! message #(str/replace % (nth array 0) (str "<a target='_blank' href='" (nth array 1) "'>" (nth array 1) "</a>"))))
    (doseq [array (re-seq #"<@(\d+|\w+)>" data)]
      (swap! message #(str/replace % (nth array 0) (str "<a class='ref' href='#'>@" (ping-name (nth array 1)) "</a>"))))
    @message))

(defn parser
  [data]
    (assoc (dissoc data :text) :text (username-parse (:text data))))

(defn- get-proper-response
  [response]
  (-> (json/parse-string response true)
      (parser)
      (ts :ts)
      (ts :thread_ts)
      (user-map)
      (channel-map)))

(defn setup-elastic
  []
  (let [config (get-config)
        es-conn (es/connect-es config)
        iexist (es/index_exist! es-conn (:index-name config))]
    (if (es/ensure-index!
          es-conn (:index-name config) es-config/settings es-config/mapping1)
      {:conn es-conn
       :index-name (:index-name config)
       :mapping (:mapping1 config)
       :index_exist iexist}
      {:index_exist iexist})))



(defn start
  [options]
  (let [conn-options (setup-elastic)
        rtm-conn (rtm/start options #(es/elastic-update-message (assoc conn-options :response % )))]
       (if (:index_exist conn-options)
         nil
         (map-db/main options (:conn conn-options) (get-config)))
       (rtm/subscribe "message"
         (fn [x]
             (es/elastic-feed (assoc conn-options :response (get-proper-response x)
               ))))
    rtm-conn))
