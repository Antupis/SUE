(ns chaty-service.service
    (:require [io.pedestal.service.http :as bootstrap]
              [io.pedestal.service.http.route :as route]
              [io.pedestal.service.http.body-params :as body-params]
              [io.pedestal.service.http.ring-middlewares :as middlewares]
              [io.pedestal.service.http.route.definition :refer [defroutes]]
              [io.pedestal.service.interceptor :refer [definterceptor]]
              [ring.util.response :as ring-resp]
              [ring.middleware.session.cookie :as cookie]))

(def ^:private streaming-contexts (atom {}))

(defn about-page [request]
  (ring-resp/response (format "Clojure %s" (clojure-version))))

(defn index-page
  [request]
  (ring-resp/response "Hello World!"))

(defn receive[request]
  (ring-resp/response "chat-page"))

(defn publish [request]
  (ring-resp/response "publish"))

(defn about-page
  [request]
  (ring-resp/response "about-page"))






;;get initial message

;;return list of active users

;;subcribe channel

;;pull messages

;;push messages
(definterceptor session-interceptor
  (middlewares/session {:store (cookie/cookie-store)}))

(defroutes routes
  [[["/" {:get index-page}
     ;; Set default interceptors for /about and any other paths under /
     ^:interceptors [(body-params/body-params) bootstrap/html-body session-interceptor]
     ["/about" {:get about-page}]
     ["/msgs" {:get receive :post publish}]]]])

;; You can use this fn or a per-request fn via io.pedestal.service.http.route/url-for
(def url-for (route/url-for-routes routes))

;; Consumed by chaty-service.server/create-server
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; :bootstrap/interceptors []
              ::bootstrap/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::boostrap/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::bootstrap/resource-path "/public"

              ;; Either :jetty or :tomcat (see comments in project.clj
              ;; to enable Tomcat)
              ;;::bootstrap/host "localhost"
              ::bootstrap/type :jetty
              ::bootstrap/port 8080})




