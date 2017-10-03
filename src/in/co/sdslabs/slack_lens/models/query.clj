(ns in.co.sdslabs.slack-lens.models.query  
    (:require [clojurewerkz.elastisch.rest.document :as esd]
              [clojurewerkz.elastisch.query :as q]
              [in.co.sdslabs.slack-lens.lib.es :as es]
              [in.co.sdslabs.slack-lens.listener.main :as main]))
(def config (main/get-config))
(def es-conn (es/connect-es config))
(def options {:conn es-conn
              :index-name (:index_name config)
              :mapping (:mapping config)})  
    
(defn search
    "search the channels"
    [channel]
    (:hits (:hits (esd/search (:conn options) 
                (:index-name options)  
                (:mapping options) 
                :query (q/term :channel "c025w23an")))))
        

    
    
    