(ns in.co.sdslabs.slack-lens.service
  (:require

   [clojure.string :as string]
   [clojure.tools.logging :refer [info]]

   [ring.middleware.content-type :refer [wrap-content-type]]
   [ring.middleware.logger :refer [wrap-with-logger]]
   [compojure.api.sweet :refer :all]
   [compojure.route :as route]
   [compojure.api.meta]
   [ring.middleware.resource :as res]
   [compojure.api.middleware :refer [api-middleware]]

   [in.co.sdslabs.slack-lens [resources :as resources]]))

(defn initialize!
  "Called once, Initialize global state before serving. This function is invoked on servlet initialization"
  []
  (info "slack-lens starting"))

(defn destroy!
  "Called once, clean up global state after serving. This function is invoked on servlet teardown"
  []
  (info "slack-lens shutting down"))

;  Wrap routes with a collection of middleware functions.
;  (swagger-ui) - adds the swagger interface for documenation and invoking the api

(defapi slack-lens-api
  ;; See docstring for details on error-handlers.
  {}
  (middlewares []
    ;; Add any internal middlewares to list above so these will be called before request
    ;; by default the ui is deployed at "/", use the path param to deploy somewhere else
               (swagger-ui)
               (swagger-docs
                {:info
                 {:title "slack-lens"
                  :description "slack-lens API"
                  :version "v1"}
                 :tags
                 [{:name "v1" :description "slack-lens API"}]})
               (context* "/v1" []
                         :tags ["v1"]
                         resources/v1_routes)
               (route/not-found "404 Not Found\n")))




(def main-handler
  (as-> slack-lens-api $
      (wrap-with-logger $)
      (res/wrap-resource $ "public")
      (wrap-content-type $)
          ;Add external middlewares here, these are called after the request handler
))
