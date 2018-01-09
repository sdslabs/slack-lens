(ns in.co.sdslabs.slack-lens.controllers.oauthHandler
  (:require
    [clj-http.client :as client]
    [cheshire.core :as json]
    [clojure.java.io :as jio]
    [in.co.sdslabs.slack-lens.models.query :as query ]
    [buddy.core.nonce :as nonce]
    [buddy.core.codecs :as codecs]
    [clj-time.local :as l]
    [clj-time.coerce :as c]
    [ring.util.response :refer [redirect]]))

(defn get-config
  []
  (-> (jio/resource "config.json")
       slurp
       (json/parse-string true)))

(defn random-cookie
  []
  (let [randomdata (nonce/random-bytes 32)]
    (codecs/bytes->hex randomdata)))


(defn codeHandle [code]
    (let [response (json/parse-string (:body (client/get "https://slack.com/api/oauth.access"
            {:query-params {:code code
                            :client_id (:client_id (get-config))
                            :client_secret (:client_secret (get-config))}}))
                    true)
            cookie (random-cookie)]
    (query/feed-user-data (assoc
        (select-keys (:user response) [:image_48 :email :name :id])
        :cookie cookie))
    {:status 200
    :headers {
                "Content-Type" "text/html"}
    :body (str "<script>var d=new Date();"
                "d.setTime("
                (+  (c/to-long (str (l/local-now))) (apply * 7 [24 3600 1000]))
                ");"
                "document.cookie = \"cookie="
                cookie
                ";expires=\" + d.toUTCString()+\";path=/\";"
                "setTimeout(function(){window.location.href='/v1/slack-lens'}, 500);</script>redirecting to home")}
    ))

(defn errorHandle
    [error]
    (str error " <br> return to <a href='/v1/slack-lens' >home-page</a>"))
