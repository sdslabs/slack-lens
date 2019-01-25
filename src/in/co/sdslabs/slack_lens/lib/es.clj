(ns in.co.sdslabs.slack-lens.lib.es
  (:require [clojurewerkz.elastisch.rest :as es]
            [clojurewerkz.elastisch.rest.index :as esi]
            [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.query :as q]
            [clojure.string :as str]
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

(defn index_exist!
  [conn index-name]
  (esi/exists? conn index-name))

(defn connect-es
  "Takes url to connect as parameter and tries to connect to it through
   rest api"
  [options]
  (try
    (es/connect (str "http://" (:host options) ":" (:port options)) {:basic-auth [(:es_user options) (:es_pass options)]})
    (catch Exception e (log/info e))))

(defn elastic-feed
  "Creates document with options passed as param and sets it's
   value as 'response' on an es 'conn', mapping 'mapping' and index 'index-name'
   These all are required keys in the options params"
  [{:keys [conn index-name response mapping]}]
  (esd/create conn index-name mapping response))



(defn elastic-update-message
  [{:keys [conn index-name response mapping]}]
  (time (Thread/sleep 1000))

    (let [found (as-> (:thread_ts response) $
        (str/lower-case $)
        (q/term :ts $)
        (esd/search conn index-name mapping :query $)
        (do (prn $) $)
        (:hits $)
        (:hits $))]
        (if (empty? found)
        nil
        (as-> found $
        (nth $ 0)
        (:_id $)
        (if (not (= "message_deleted" (:subtype response)))
          (esd/update-with-script conn
            index-name
            mapping
            $ (str "ctx._source.replies = " (:replies response)))
          (esd/update-with-script conn
            index-name
            mapping
            $ (str "ctx._source.deleted = " true)))))))

(defn elastic-edit-message
  [{:keys [conn index-name response mapping]}]
    (time (Thread/sleep 1000))
    (let [found (as-> (:edited_ts response) $
        (str/lower-case $)
        (q/term :ts $)
        (esd/search conn index-name mapping :query $)
        (:hits $)
        (:hits $))]
        (if (empty? found)
        nil
        (do (as-> found $
        (nth $ 0)
        (:_id $)
          (esd/update-with-script conn
            index-name
            mapping
            $ (str "ctx._source.edited = " true)))))))

(defn thread-and-edit
  [query]
  (if (get-in query [:response :edited_ts])
    (elastic-edit-message query)
    (elastic-update-message query)))

(defn store-user-data
  [{:keys [conn index-name response mapping]}]
  (if (:id response)
      (let [present (as-> (:id response) $
            (str/lower-case $)
            (q/term :id $)
            (array-map :query $)
            (esd/search conn index-name mapping $)
            (:hits $)
            (do (prn $) $)
            (:hits $))]
        (if (not (empty? present))
          (esd/update-with-script conn
              index-name
              mapping (:_id (nth  present 0))
              (str "ctx._source.access_token = \"" (:access_token response) "\""))
          (esd/create conn index-name mapping response)))
      nil))

(defn store-cookie
  [{:keys [conn index-name response mapping]}]
  (esd/create conn index-name mapping response))

(defn delete-cookie
  [{:keys [conn index-name cookie mapping]}]
      (let [row (as-> (str/lower-case cookie) $
            (q/term :cookie $)
            (esd/search conn index-name mapping :query $)
            (:hits $)
            (:hits $))]
          (if (not (empty? row))
            (as-> row $
                (nth $ 0)
                (:_id $)
                (esd/delete conn index-name mapping $)
                (do (prn $) true))
            false)))

(defn elastic-delete
  "Creates document with options passed as param and sets it's
   value as 'response' on an es 'conn' and index 'index-name'
   These all are required keys in the options params"
  [{:keys [conn index-name response id mapping]}]
  (esd/delete conn index-name mapping id))

(defn search-user
    "search the user"
    [id conn map1]
    (as-> (str/lower-case id) $
          (q/term :id $)
          (esd/search conn (:index-name map1) (:mapping2 map1) :query $)
          (:hits $)
          (:hits $)
          (nth $ 0)
          (:_source $)))

(defn search-channel
    "search the channel"
    [id conn map2]
    (as-> (str/lower-case id) $
          (q/term :id $)
          (esd/search conn (:index-name map2) (:mapping3 map2) :query $)
          (:hits $)
          (:hits $)
          (nth $ 0)
          (:_source $)))

