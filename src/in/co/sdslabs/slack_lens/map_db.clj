(ns in.co.sdslabs.slack-lens.map-db
  (:require [in.co.sdslabs.slack-lens.lib.es :as es]
         [in.co.sdslabs.slack-lens.config.es :as es-config]
         [clj-slack.users :as user]
         [clj-slack.channels :as channels]))


(def conn (atom {}))



(defn insert-users
  [user-type]
  (doseq [x (map #(conj 
                    (select-keys % [:id :name ]) 
                    (select-keys (get % :profile) [:image_48])) 
                  (:members (user/list @conn)))] (es/elastic-feed  (assoc user-type :response x))))

(defn insert-channels
  [channel-type]
  (doseq [x (map #(select-keys % [:id :name]) 
                 (:channels (channels/list @conn)))] 
                   (es/elastic-feed (assoc channel-type :response x))))

(defn main
      [options es-conn config]
      (let [user-type {:conn es-conn :index-name (:index_name config)  :mapping (:mapping2 config)}
        channel-type {:conn es-conn :index-name (:index_name config)  :mapping (:mapping3 config)}]
      (swap! conn conj {:api-url (:url options) :token (:token options)})
      (insert-users user-type)
      (insert-channels channel-type)))
