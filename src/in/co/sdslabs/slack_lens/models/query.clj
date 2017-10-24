(ns in.co.sdslabs.slack-lens.models.query  
    (:require [clojurewerkz.elastisch.rest.document :as esd]
              [clojurewerkz.elastisch.query :as q]
              [in.co.sdslabs.slack-lens.lib.es :as es]
              [in.co.sdslabs.slack-lens.listener.main :as main]))

(def config (main/get-config))
(def es-conn (es/connect-es config))
 
    
(defn search
    "search the channels"
    [channel from size keymap]
    (:hits (:hits (esd/search es-conn 
                (:index_name config)  
                (:mapping1 config) 
                :query (q/term keymap channel)
                :from from :size size))))

(defn search-miss
    "search the channels"
    [channel from size keymap]
    (:hits (:hits (esd/search es-conn 
                (:index_name config)  
                (:mapping1 config) 
                :query (q/term keymap channel)
                :from from :size size))))


    
(defn ch-search
    "search the channels"
    [from size]
    (:hits (:hits (esd/search es-conn 
                (:index_name config)  
                (:mapping3 config) 
                :query (q/match-all)
                :from from :size size))))