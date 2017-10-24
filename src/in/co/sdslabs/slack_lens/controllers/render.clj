(ns in.co.sdslabs.slack-lens.controllers.render
  (:require
    [clostache.parser :as clostache]
    [in.co.sdslabs.slack-lens.models.query :as query]
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

(defn css [filename]
    (render-template filename {}))

(defn thread [filename thread_ts]
    (println thread_ts)
    (render-template  filename {:data {:active thread_ts
                                     :messages (query/search (str/lower-case thread_ts) 0 100 :thread_ts)}}))