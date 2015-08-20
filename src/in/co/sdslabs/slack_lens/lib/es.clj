(ns in.co.sdslabs.slack-lens.lib.es
  (:require [clojurewerkz.elastisch.rest :as es]
            [clojurewerkz.elastisch.rest.index :as esi]
            [clojurewerkz.elastisch.rest.document :as esd]
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
  "Takes url to connect as parameter and tries to connect to it through
   rest api"
  [options]
  (try
    (es/connect (:url options))
    (catch Exception e (log/info e))))

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
