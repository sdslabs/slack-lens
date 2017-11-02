(ns in.co.sdslabs.slack-lens.resources
  (:require
    [ring.util.http-response :as http]
    [compojure.api.sweet :refer [defroutes* GET*]]
    [compojure.api.meta]
    [in.co.sdslabs.slack-lens.controllers.render :as render]))

(defroutes* v1_routes
  (GET*
    "/slack-lens"
    []
    :summary "Dummy Route to index"
    :description
    "<p>Add a description here</p>"
    :query-params [channel :- String]
    (render/mustache "slack.mustache" channel))
  
  (GET*
    "/slack.css"
    []
    :summary "Dummy Route to index"
    :description
    "<p>Add a description here</p>"
    (render/static "slack.css"))

  (GET*
    "/jquery.min.js"
    []
    :summary "Dummy Route to index"
    :description
    "<p>Add a description here</p>"
    (render/static "jquery.min.js"))

  (GET*
    "/thread"
    []
    :summary "Dummy Route to index"
    :description
    "<p>Add a description here</p>"
    :query-params [thread_ts :- Double]
    (render/thread "thread.mustache" thread_ts))

  (GET*
    "/data"
    []
    :summary "Dummy Route to index"
    :description
    "<p>Add a description here</p>"
    :query-params [date :- String, channel :- String]
    (render/date-range "empty-file" date channel)))
