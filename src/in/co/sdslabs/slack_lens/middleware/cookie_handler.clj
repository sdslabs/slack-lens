(ns in.co.sdslabs.slack-lens.middleware.cookie-handler
  (:require
    [clojure.string :as str]
    [in.co.sdslabs.slack-lens.models.query :as query]))


(defn- cookie-present?
  [cookie-str]
  (if cookie-str
      (not= (.indexOf cookie-str "cookie=") -1)
      false))

(defn- get-cookie
  [cookie-str]
  (if-let [cookie-arr (re-find #"cookie=([0-9|a-f]{64})" cookie-str)]
           (nth cookie-arr 1)
           false))

(defn- add-authenticated
  [request]
    (let [cookie-str (get (:headers request) "cookie")]
      (if (cookie-present? cookie-str)
          (if-let [cookie (get-cookie cookie-str)]
            (if (query/validate-cookie cookie)
                (assoc request :authenticated true :cookie cookie)
                (assoc request :authenticated false))
                (assoc request :authenticated false))
          (assoc request :authenticated false))))

(defn wrap-cookies
  [handler]
  (fn [request]
    (handler (add-authenticated request))))


