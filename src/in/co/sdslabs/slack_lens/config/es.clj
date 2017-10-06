(ns in.co.sdslabs.slack-lens.config.es)


(def settings {"number_of_replicas" 0
               "number_of_shards" 1 })

(def mapping1 {
    "message" {
      :_all { :enabled true }

      :_id { :path "id" }

      :_timestamp { :enabled true :path "timestamp" }

      :properties {
        :id { :type "integer" }
        :type { :type "string" }
        :channel { :type "string" }
        :user { :type "string" }
        :text { :type "string" }
        :timstamp { :type "long" }
        :team { :type "string" }}}})

(def mapping2 {
    "pehchankaun" {
      :_all { :enabled true }

      :_id { :path "id" }

      :_timestamp { :enabled true :path "timestamp" }

      :properties {
        :id { :type "string" }
        :user { :type "string" }
        :image_48 { :type "string" }}}})

(def mapping3 {
    "channels" {
      :_all { :enabled true }

      :_id { :path "id" }

      :_timestamp { :enabled true :path "timestamp" }

      :properties {
        :id { :type "string" }
        :name { :type "string" }}}})