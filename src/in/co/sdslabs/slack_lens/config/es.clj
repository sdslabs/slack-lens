(ns in.co.sdslabs.slack-lens.config.es)


(def settings {"number_of_replicas" 0
               "number_of_shards" 1 })

(def mappings {
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