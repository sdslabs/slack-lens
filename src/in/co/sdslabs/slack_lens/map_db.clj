(ns in.co.sdslabs.slack-lens.map-db
  (:require [in.co.sdslabs.slack-lens.lib.es :as es]
         [in.co.sdslabs.slack-lens.config.es :as es-config]
         [in.co.sdslabs.slack-lens.listener.main :as main]
         [clj-slack.users :as user]
         [clj-slack.channels :as channels]))


(def conn (atom nil))
(def config (main/get-config))
(def es-conn (es/connect-es config))
(def user-type {:conn es-conn :index-name (:index_name config)  :mapping (:mapping2 config)})
(def channel-type {:conn es-conn :index-name (:index_name config)  :mapping (:mapping3 config)})

(defn create 
     []
     (es/ensure-index! 
        es-conn (:index_name config) 
                es-config/settings 
                es-config/mapping2)
     (es/ensure-index! 
        es-conn (:index_name config) 
                es-config/settings 
                es-config/mapping3))

(defn insert-users
  []
  (doseq [x (map #(conj 
                    (select-keys % [:id :name ]) 
                    (select-keys (get % :profile) [:image_48])) 
                  (:members (user/list conn)))] (es/elastic-feed  (assoc user-type :response x))))

(defn insert-channels
  []
  (doseq [x (map #(select-keys % [:id :name]) 
                 (:channels (channels/list conn)))] 
                   (es/elastic-feed (assoc channel-type :response x))))

(defn main
      [options]
      (swap! conn conj {:apt-url (:url options) :token (:token options)})
      (create)
      (insert-users)
      (insert-channels))
