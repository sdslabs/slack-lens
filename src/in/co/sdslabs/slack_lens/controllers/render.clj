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

(defn render_css [css-file]
  (clostache/render
    (slurp (clojure.java.io/resource
          (str "css/" css-file))) {}))

(defn render_js [js-file]
  (clostache/render
    (slurp (clojure.java.io/resource
            (str "js/" js-file))) {}))

(defn render-template [template-file params]
  (clostache/render (read-template template-file) params))

(defn mustache [filename active]
  (render-template  filename {:data {:active active
                                     :slack-name (:slack-name query/config)
                                     :channels (query/ch-search 0 100)}}))
(defn message [filename channel]
   (render-template filename {:data (json/generate-string 
                     {:messages (query/search-miss (str/lower-case channel) 0 100 :channel)})}))

(defn css [filename]
  (render_css filename))

(defn js [filename]
  (render_js filename))

(defn thread [filename thread_ts]
  (render-template  filename {:data (json/generate-string {
                                      :messages (query/search thread_ts 0 100 :thread_ts)})}))

(defn date-range [filename date channel length]
  (let [multi-parser (f/formatter (t/default-time-zone) "YYYY-MM-dd" "dd/MM/YYYY")]
    (render-template  filename {:data (json/generate-string
                                       {:messages (query/date-search
                                                   (/ (c/to-long
                                                       (f/unparse multi-parser (f/parse multi-parser date)))
                                                      1000.0)
                                                   (* 86400 length)
                                                   channel 0 100 :ts)})})))

(defn userMes
  [filename person channel]
  (render-template filename {:data (json/generate-string {:messages (query/user-message person channel 0 100)})}))