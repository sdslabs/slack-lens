(ns in.co.sdslabs.slack-lens.controllers.render
  (:require
   [clostache.parser :as clostache]
   [in.co.sdslabs.slack-lens.models.query :as query]
   [cheshire.core :as json]
   [clj-time.format :as f]
   [clj-time.coerce :as c]
   [clj-time.core :as t]
   [clojure.string :as str]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;rendering views for routes                   ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; helper functions

(def no-of-messages 25 )

(defn date-format
  [date]
  (let [multi-parser (f/formatter (t/default-time-zone) "YYYY-MM-dd" "dd/MM/YYYY")]
    (/ (c/to-long
       (f/unparse multi-parser (f/parse multi-parser date)))
       1000.0)))

(defn read-template [template-name]
  (slurp (clojure.java.io/resource
          (str "views/" template-name))))

(defn render-template [template-file params]
  (clostache/render (read-template template-file) params))


;; render functions
(defn mustache [filename active cookie]
  (as-> {:active active
         :slack-name (:slack-name query/config)
         :user (query/user-info cookie)
         :channels (query/ch-search 0 100)} $
  (array-map :data $)
  (render-template filename $)))

(defn message [filename channel start]
  (as-> (query/search-miss (str/lower-case channel) start no-of-messages :channel) $
      (array-map :messages $)
      (json/generate-string $)
      (array-map :data $)
      (render-template filename $)))

(defn thread [filename thread_ts]
  (as-> (query/search thread_ts 0 100 :thread_ts) $
      (array-map :messages $)
      (json/generate-string $)
      (array-map :data $)
      (render-template filename $)))

(defn userMes
  [filename person channel]
    (as-> (query/user-message person channel 0 100) $
        (array-map :messages $)
        (json/generate-string $)
        (array-map :data $)
        (render-template filename $)))

(defn date-range [filename date channel length start]
  (let [multi-parser (f/formatter (t/default-time-zone) "YYYY-MM-dd" "dd/MM/YYYY")]
    (as-> (query/date-search (date-format date) (* 86400 length) channel start no-of-messages :ts) $
        (array-map :messages $)
        (json/generate-string $)
        (array-map :data $)
        (render-template filename $))))

