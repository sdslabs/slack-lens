(ns in.co.sdslabs.slack-lens.lib.es
  (:require [clojurewerkz.elastisch.native :as es]
            [clojurewerkz.elastisch.native.index :as esi]
            [clojurewerkz.elastisch.native.document :as esd]
            [clojure.tools.logging :as log]))

(defn ensure-index!
  "Returns boolean according to if es index exists or not, if es index is already exists we move on returning true
   Else we continue and try to create the index, finally we return true if we were successful else we return false
   indicating our failure"
  [conn index-name settings mappings]
  (if (esi/exists? conn index-name)
    (do
      (log/info "Index already exists. Moving on") ; Already exists, return true
      true)
    (do
      (log/info "Creating index: " index-name)
        (esi/create conn index-name :settings settings :mappings mappings) ;Create the index
        (esi/refresh conn index-name) ; Refresh so we all or on the same page
        (if (esi/exists? conn index-name) ; Lets check if we have the index now
          (do
            (log/info index-name " successfully created.") ; We have the index lets return true
            true)
          (do
            (log/info "Something happened, failed to create.") ; We don't have the index we must return false
            false))
        )))

(defn connect-es
  "Takes options map as parameter and tries to establish connection to the es node
   Options is a map with the following keys
    :port Port number where the updator should connect for es [Since this is the
          native client so in usual cases this will be 9300
    :url Url of elasticsearch [Locally 127.0.0.1]
    :clustername Name of the elasticsearch cluster [Usually 'elasticsearch']"
  [options]
  (let [{:keys [port url clustername]} options]
    (try
      (es/connect [[url port]] {"cluster.name" clustername})
      (catch Exception e (log/info e)))))

(defn elastic-feed
  "Creates document with options passed as param and sets it's
   value as 'response' on an es 'conn', mapping 'mapping' and index 'index-name'
   These all are required keys in the options params"
  [{:keys [conn index-name response mapping]}]
  (esd/create conn index-name mapping response))

(defn elastic-delete
  "Creates document with options passed as param and sets it's
   value as 'response' on an es 'conn' and index 'index-name'
   These all are required keys in the options params"
  [{:keys [conn index-name response id mapping]}]
  (esd/delete conn index-name mapping id))
