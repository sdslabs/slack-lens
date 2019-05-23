(ns in.co.sdslabs.slack-lens.resources
  (:require
    [ring.util.http-response :as http]
    [compojure.api.sweet :refer [defroutes* GET*]]
    [compojure.api.meta]
    [clojure.string :as str]
    [in.co.sdslabs.slack-lens.controllers.oauthHandler :as oauth]
    [in.co.sdslabs.slack-lens.models.query :as query]
    [ring.util.response :refer [redirect]]
    [in.co.sdslabs.slack-lens.controllers.render :as render]))

(defroutes* redir
  (GET* "/" [] (redirect "/v1/slack-lens")))

(defroutes* v1_routes
  (GET*
    "/slack-lens"
    request
    :summary "route for homepage"
    (if (:authenticated request)
      (let [cookie (:cookie request) params (:query-params request)]
        (cond
          ;; when channel is present in query-string
          (contains? params "channel")
            (render/mustache "slack.mustache" (get params "channel") cookie)
          (not (contains? params "channel"))
            (render/mustache "slack.mustache" "general" cookie)))
        (render/render-template "home.mustache" {:id (:client_id (oauth/get-config))
                                                 :slack-name (:slack-name query/config)})))

  (GET*
    "/thread"
    []
    :summary "data for the thread related messages"
    :query-params [thread_ts :- Double]
    (render/thread "empty-file" thread_ts))

  (GET*
    "/edited"
    []
    :summary "data for the thread related messages"
    :query-params [edited_ts :- Double]
    (render/edited "empty-file" edited_ts))

  (GET*
  "/slack/oauth"
  request
  :summary "authentization grant handling"
  (let [ x (:query-params request)]
      (cond
        (contains? x "code") (oauth/codeHandle (get x "code"))
        (contains? x "error") (oauth/errorHandle (get x "error"))
        :else nil)))


  (GET*
    "/logout"
    request
    :summary "logout"
    (let [cookie (:cookie request)]
      (cond
        ;; when user-cookie is passed
        cookie
          (do (query/logout cookie)
            {:body  "user logged out"
                      :headers { "Content-Type" "text/html" }
                      :status 204})
          :else {:body  "cookie is invalid."
                      :headers { "Content-Type" "text/html" }
                      :status 400})))

  (GET*
    "/channel"
    []
    :summary "passing channel's name list"
    :query-params [channel :- String, start :- Long]
    (render/message "empty-file" channel start))

  (GET*
    "/usermes"
    []
    :summary "ajax request for getting messages is handled here"
    :query-params [person :- String, channel :- String]
    (render/userMes "empty-file" person channel))

  (GET*
    "/data"
    []
    :summary "filtering the messages by date"
    :query-params [date :- String, channel :- String, length :- Long, start :- Long]
    (render/date-range "empty-file" date channel length start))

  (GET*
    "/search"
    []
    :summary "search by text"
    :query-params [search-text :- String, channel :- String]
    (render/search "empty-file" search-text channel)))
