(ns in.co.sdslabs.slack-lens.config.es)

(def settings {"number_of_replicas" 0
               "number_of_shards" 1})

(def mapping1 {"message" {



                          :properties {:id {:type "text"}
                                       :type {:type "text"}
                                       :channel {:type "text"}
                                       :user {:type "text"}
                                       :image_48 {:type "text"}
                                       :text {:type "text"}
                                       :ts {:type "double"}
                                       :team {:type "text"}
                                       :replies {:type "integer"
                                                 :null_value 0}
                                       :thread_ts {:type "double"}}}

,"pehchankaun" {



                              :properties {:id {:type "text"}
                                           :user {:type "text"}
                                           :image_48 {:type "text"}}}

,"channels" {


                           :properties {:id {:type "text"}
                                        :name {:type "text"}}}

,"user-details" {



                           :properties {:id {:type "text"}
                                        :name {:type "text"}
                                        :image_48 {:type "text"}
                                        :team {:type "text"}
                                        :email {:type "text"}
                                        :access_token {:type "text"}}}

,"login" {


                           :properties {:id {:type "text"}
                                        :team {:type "text"}
                                        :cookie {:type "text"}}}})
