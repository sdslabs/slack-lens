(ns in.co.sdslabs.slack-lens.controllers.render
  (:require
   [clostache.parser :as clostache]
   [in.co.sdslabs.slack-lens.models.query :as query]
   [cheshire.core :as json]
   [clj-time.format :as f]
   [clj-time.coerce :as c]
   [clj-time.core :as t]
   [clojure.string :as str]))

(defn read-template [template-name]
  (slurp (clojure.java.io/resource
          (str "views/" template-name))))

(defn render-template [template-file params]
  (clostache/render (read-template template-file) params))

(defn mustache [filename active]
  (render-template  filename {:data {:active active
                                     :messages (query/search-miss (str/lower-case active) 0 100 :channel)
                                     :channels (query/ch-search 0 100)}}))

(defn static [filename]
  (render-template filename {}))

(defn thread [filename thread_ts]
  (println thread_ts)
  (render-template  filename {:data {:active thread_ts
                                     :messages (query/search thread_ts 0 100 :thread_ts)}}))

(defn date-range [filename date channel]
  (let [multi-parser (f/formatter (t/default-time-zone) "YYYY-MM-dd" "dd/MM/YYYY")]
    (prn {:data (json/generate-string
                 {:messages (query/date-search
                             (/ (c/to-long
                                 (f/unparse multi-parser (f/parse multi-parser date)))
                                1000.0)
                             channel
                             0 100
                             :ts)})})
    (render-template  filename {:data (json/generate-string
                                       {:messages (query/date-search
                                                   (/ (c/to-long
                                                       (f/unparse multi-parser (f/parse multi-parser date)))
                                                      1000.0)
                                                   channel 0 100 :ts)})})))
