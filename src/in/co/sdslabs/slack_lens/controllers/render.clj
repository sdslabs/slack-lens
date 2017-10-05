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

(defn index [filename]
  (render-template  filename {:data {:messages (query/search (str/lower-case "C025W23AN")) :channels [{:channel "ch1" }{:channel "ch2"}]}}))