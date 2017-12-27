(ns in.co.sdslabs.slack-lens.resources
  (:require
    [ring.util.http-response :as http]
    [compojure.api.sweet :refer [defroutes* GET*]]
    [compojure.api.meta]
    [clojure.string :as str]
    [in.co.sdslabs.slack-lens.controllers.oauthHandler :as oauth]
    [in.co.sdslabs.slack-lens.models.query :as query]
    [in.co.sdslabs.slack-lens.controllers.render :as render]))


(defroutes* v1_routes
  (GET*
    "/slack-lens"
    request
    :summary "the html structure fof the web-interface"
   (let [ x (:query-params request) [type cookie] (str/split (get (:headers request) "cookie") #"=")]
   (if (query/validate-cookie type cookie)
        (cond
          (contains? x "channel")
              (render/mustache "slack.mustache" (get x "channel"))
          (not (contains? x "channel"))
              (render/mustache "slack.mustache" "general" cookie))
              (render/html "home.mustache"))))

  (GET*
    "/slack.css"
    []
    :summary "the slack-lens front end css"
    (render/css "slack.css"))

  (GET*
    "/slack.js"
    []
    :description
    "the javascript code for the dynamic functionality in web-interface"
    (render/js "slack.js"))

  (GET*
    "/thread"
    []
    :summary "data for the thread related messages"
    :query-params [thread_ts :- Double]
    (render/thread "empty-file" thread_ts))

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
    "/channel"
    []
    :summary "passing channel's name list"
    :query-params [channel :- String]
    (render/message "empty-file" channel))

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
    :query-params [date :- String, channel :- String, length :- Long]
    (render/date-range "empty-file" date channel length)))
