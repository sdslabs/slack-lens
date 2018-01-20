(ns in.co.sdslabs.slack-lens.controllers.oauthHandler
  (:require
    [clojure.java.io :as jio]

    [clj-http.client :as client]

    [cheshire.core :as json]

    [buddy.core.nonce :as nonce]
    [buddy.core.codecs :as codecs]

    [clj-time.local :as l]
    [clj-time.coerce :as c]

    [in.co.sdslabs.slack-lens.models.query :as query]
    [in.co.sdslabs.slack-lens.controllers.render :as render]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;               oauth handler                ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; helper functions
(defn get-config
  []
  (-> (jio/resource "config.json")
       slurp
       (json/parse-string true)))

(defn random-cookie
  []
  (let [randomdata (nonce/random-bytes 32)]
    (codecs/bytes->hex randomdata)))

(defn get-current-time
  []
  (as-> (l/local-now) $
      (str $)
      (c/to-long $)
      (+ $ (apply * 7 [24 3600 1000]))))

(defn redirect
  [cookie]
  (render/render-template "redirect.mustache"
                          {:time (get-current-time)
                           :cookie cookie}))


;; function for handling auth
(defn codeHandle
  [code]
  (let  [response  (as-> (get-config) $
                         (select-keys $ [:client_id :client_secret])
                         (assoc $ :code code)
                         (array-map :query-params $)
                         (client/get "https://slack.com/api/oauth.access" $)
                         (:body $)
                         (json/parse-string $ true))
         cookie (random-cookie)]

    (as-> (:user response) $
          (select-keys $ [:image_48 :email :name :id])
          (assoc $ :team (get-in response [:team :id]) :access_token (:access_token response))
          (query/feed-user-data $))

    (as-> {:cookie cookie} $
        (assoc $ :id (get-in response [:user :id]))
        (assoc $ :team (get-in response [:team :id]))
        (query/feed-cookie $))

    {:status 200
    :headers { "Content-Type" "text/html" }
    :body (redirect cookie)}))

(defn errorHandle
    [error]
    (str error " <br> return to <a href='/v1/slack-lens' >home-page</a>"))
