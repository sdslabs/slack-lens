(ns in.co.sdslabs.slack-lens.models.query
  (:require [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.query :as q]
            [in.co.sdslabs.slack-lens.lib.es :as es]
            [clojure.set :refer [rename-keys]]
            [clojure.string :as str]
            [in.co.sdslabs.slack-lens.listener.main :as main]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; client side query handler            ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; some useful constants
(def config (main/get-config))
(def es-conn (es/connect-es config))


(defn search
  "search the channels"
  [channel from size keymap]
  (as-> (esd/search es-conn
          (:index-name config)
          (:mapping1 config)
          :query (q/term keymap channel)
          :from from :size size) $
      (:hits $)
      (:hits $)))

(defn search-miss
  "search the channels"
  [channel from size keymap]
  (-> (esd/search es-conn
          (:index-name config)
          (:mapping1 config)
          :query (q/term keymap channel)
          :sort {:ts {:order :desc}}
          :filter {:missing {:field [:thread_ts :edited_ts]}}
          :from from :size size)
      (:hits)
      (:hits)))

(defn id-team
  [data]
  [(as-> (:id data) $
         (str/lower-case $)
         (q/term :id $))

   (as-> (:team data) $
         (str/lower-case $)
         (q/term :team $))])


(defn- get-user-details
 [arg]
  (as-> arg $
    (array-map :must $)
    (array-map :bool $)
    (array-map :filter $)
    (array-map :filtered $)
    (esd/search es-conn
      (:index-name config)
      (:mapping4 config) :query $)
    (:hits $)
    (:hits $)
    (nth $ 0)))

(defn user-info
  "get user details"
  [cookie]
  (as-> (esd/search es-conn
          (:index-name config)
          (:mapping5 config)
          :query (q/term :cookie cookie)) $
        (:hits $)
        (:hits $)
        (nth $ 0)
        (:_source $)
        (select-keys $ [:id :team])
        (id-team $)
        (get-user-details $)))


;; searching using multiple conditions
(defn- search-by-multiple-conditions
  [arg from size]
  (as-> arg $
  (array-map :must $)
  (array-map :bool $)
  (array-map :filter $)
  (array-map :filtered $)
  (esd/search es-conn
      (:index-name config)
      (:mapping1 config)
      :query $
      :sort {:ts {:order :desc}}
      :from from :size size)
  (:hits $)
  (:hits $)))


(defn date-search
  "search the channels"
  [ts length channel from size keymap]
  (search-by-multiple-conditions
      [(q/range keymap {:gte ts})
       (if (= 0 length)
            nil
            (q/range keymap
                {:lte (+ ts length)}))
       (q/term :channel channel)
       {:missing {:field :thread_ts}}]
      from size))

(defn user-message
  "search the channels"
  [person channel from size]
  (search-by-multiple-conditions
      [(q/term :channel channel)
       (q/term :name person)
       {:missing {:field :thread_ts}}]
      from size))


(defn ch-search
  "search the channels"
  [from size]
  (-> (esd/search es-conn
          (:index-name config)
          (:mapping3 config)
          :query (q/match-all)
          :from from :size size)
      (:hits )
      (:hits )))


(defn feed-user-data
  "data of the user is feeded"
  [data]
  (as-> config $
    (select-keys $ [:index-name :mapping4])
    (assoc $ :response data
             :conn es-conn)
    (rename-keys $ { :mapping4 :mapping })
    (es/store-user-data $)))

(defn validate-cookie
  [cookie]
  (as-> (esd/search  es-conn
              (:index-name config)
              (:mapping5 config)
              :query (q/term :cookie cookie)) $
        (:hits $)
        (:hits $)
        (empty? $)
        (not $)))

(defn feed-cookie
  [data]
  (as-> config $
    (select-keys $ [:index-name :mapping5])
    (assoc $ :response data
             :conn es-conn)
    (rename-keys $ {:mapping5 :mapping})
    (es/store-cookie $)))


(defn logout
  [cookie]
  (as-> config $
    (select-keys $ [:index-name :mapping5])
    (assoc $ :cookie cookie
             :conn es-conn)
    (rename-keys $ {:mapping5 :mapping})
    (es/delete-cookie $)))
