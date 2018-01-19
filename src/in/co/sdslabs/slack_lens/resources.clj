(ns in.co.sdslabs.slack-lens.resources
  (:require
    [ring.util.http-response :as http]
    [compojure.api.sweet :refer [defroutes* GET*]]
    [compojure.api.meta]
    [clojure.string :as str]
    [in.co.sdslabs.slack-lens.controllers.oauthHandler :as oauth]
    [in.co.sdslabs.slack-lens.models.query :as query]
    [in.co.sdslabs.slack-lens.controllers.render :as render]))


(defn cookie-present?
  [cookie-str]
  (if cookie-str
      (not= (.indexOf cookie-str "cookie=") -1)
      false))

(defn get-cookie
  [cookie-str]
  (-> cookie-str
      (str/split #"cookie=")
      (nth 1)
      (str/split #"; ")
      (nth 0)))

(defroutes* v1_routes
  (GET*
    "/slack-lens"
    request
    :summary "route for homepage"
    (if (:authenticated request)
      (let [cookie (:cookie request) params (:query-params request)]
        (cond
        ;; where channel is present in query-string
          (contains? params "channel")
            (render/mustache "slack.mustache" (get params "channel") cookie)
          (not (contains? params "channel"))
            (render/mustache "slack.mustache" "general" cookie)))
        (render/html "home.mustache")))

  (GET*
    "/slack.css"
    []
    :summary "the slack-lens front end css"
    (render/css "slack.css"))

  (GET*
    "/home.css"
    []
    :summary "css for slack-lens home page"
    (render/css "home.css"))

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
