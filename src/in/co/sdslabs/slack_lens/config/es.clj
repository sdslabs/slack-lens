(ns in.co.sdslabs.slack-lens.config.es)

(def settings {"number_of_replicas" 0
               "number_of_shards" 1})

(def mapping1 {"message" {:_all {:enabled true}

                          :_id {:path "id"}

                          :_timestamp {:enabled true :path "timestamp"}

                          :properties {:id {:type "integer"}
                                       :type {:type "string"}
                                       :channel {:type "string"}
                                       :user {:type "string"}
                                       :image_48 {:type "string"}
                                       :text {:type "string"}
                                       :ts {:type "double"}
                                       :team {:type "string"}
                                       :replies {:type "integer"
                                                 :null_value 0}
                                       :thread_ts {:type "double"}}}})

(def mapping2 {"pehchankaun" {:_all {:enabled true}

                              :_id {:path "id"}

                              :_timestamp {:enabled true :path "timestamp"}

                              :properties {:id {:type "string"}
                                           :user {:type "string"}
                                           :image_48 {:type "string"}}}})

(def mapping3 {"channels" {:_all {:enabled true}

                           :_id {:path "id"}

                           :_timestamp {:enabled true :path "timestamp"}

                           :properties {:id {:type "string"}
                                        :name {:type "string"}}}})

(def mapping4 {"login" {:_all {:enabled true}

                           :_id {:path "id"}

                           :_timestamp {:enabled true :path "timestamp"}

                           :properties {:id {:type "string"}
                                        :name {:type "string"}
                                        :image_48 {:type "string"}
                                        :token {:type "string"}
                                        :email {:type "string"}
                                        :access_token {:type "string"}
                                        }
                                        }})
