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
    (render/index "slack.mustache"))
  
  (GET*
    "/slack.css"
    []
    :summary "Dummy Route to index"
    :description
    "<p>Add a description here</p>"
    (render/index "slack.css")))
