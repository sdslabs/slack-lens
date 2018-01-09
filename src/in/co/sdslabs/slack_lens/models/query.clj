(ns in.co.sdslabs.slack-lens.models.query
  (:require [clojurewerkz.elastisch.rest.document :as esd]
            [clojurewerkz.elastisch.query :as q]
            [in.co.sdslabs.slack-lens.lib.es :as es]
            [clojure.set :refer [rename-keys]]
            [in.co.sdslabs.slack-lens.listener.main :as main]))

(def config (main/get-config))
(def es-conn (es/connect-es config))

(defn search
  "search the channels"
  [channel from size keymap]
  (:hits (:hits (esd/search es-conn
                            (:index-name config)
                            (:mapping1 config)
                            :query (q/term keymap channel)
                            :from from :size size))))

(defn search-miss
  "search the channels"
  [channel from size keymap]
  (:hits (:hits (esd/search es-conn
                            (:index-name config)
                            (:mapping1 config)
                            :query (q/term keymap channel)
                            :filter {:missing {:field :thread_ts}}
                            :from from :size size))))

(defn date-search
  "search the channels"
  [ts length channel from size keymap]
  (prn ts)
  (prn keymap)
  (:hits (:hits (esd/search es-conn
                            (:index-name config)
                            (:mapping1 config)
                            :query {:filtered {:filter {:bool {:must [(q/range keymap {:gte ts})
                                                                      (if (= 0 length) nil (q/range keymap {:lte (+ ts length)}))
                                                                      (q/term :channel channel)
                                                                      {:missing {:field :thread_ts}}]}}}}
                            :from from :size size))))

(defn user-message
  "search the channels"
  [person channel from size]
  (:hits (:hits (esd/search es-conn
                            (:index-name config)
                            (:mapping1 config)
                            :query {:filtered {:filter {:bool {:must [(q/term :channel channel)
                                                                      (q/term :name person)
                                                                      {:missing {:field :thread_ts}}]}}}}
                            :from from :size size))))

(defn user-info
  "get user details"
  [cookie]
  (nth (:hits (:hits (esd/search es-conn
                            (:index-name config)
                            (:mapping4 config)
                            :query (q/term :cookie cookie))))
                            0))

(defn ch-search
  "search the channels"
  [from size]
  (:hits (:hits (esd/search es-conn
                            (:index-name config)
                            (:mapping3 config)
                            :query (q/match-all)
                            :from from :size size))))


(defn feed-user-data
  "data of the user is feeded"
  [data]
  (es/elastic-update (rename-keys (assoc (select-keys config [:index-name :mapping4])
                          :response data :conn es-conn) { :mapping4 :mapping })))


(defn validate-cookie
  [cookie]
    (not (empty? (:hits (:hits (esd/search  es-conn
                    (:index-name config)
                    (:mapping4 config)
                    :query (q/term :cookie cookie)))))))

