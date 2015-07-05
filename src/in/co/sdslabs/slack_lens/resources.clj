(ns in.co.sdslabs.slack-lens.resources
  (:require
    [ring.util.http-response :as http]
    [compojure.api.sweet :refer [defroutes* GET*]]
    [compojure.api.meta]))

(defroutes* v1_routes
  (GET*
    "/slack-lens"
    []
    :summary "Dummy Route to index"
    :description
    "<p>Add a description here</p>"
    (http/ok "OK")))
