(ns picture-gallery.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [picture-gallery.routes.home :refer [home-routes]]
            [noir.util.middleware :as noir-middleware]
            [picture-gallery.routes.auth :refer [auth-routes]]
            [picture-gallery.routes.upload :refer [upload-routes]]
            [picture-gallery.routes.gallery :refer [gallery-routes]]
            [noir.session :as session]
            [taoensso.timbre :as timbre]
            [com.postspectacular.rotor :as rotor]
            [ring.middleware.format :refer [wrap-restful-format]]
            ))

(defn init []
  (timbre/set-config! [:timestamp-pattern] "yyyy-MM-dd HH:mm:ss")
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false
     :max-message-per-msecs nil
     :fn rotor/append})
  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "error.log" :max-size (* 512 1024) :backlog 10})
  
  (timbre/info "picture-gallery started successfully"))

(defn destroy []
  (timbre/info "picture-gallery is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn user-page [_]
  (session/get :user))

(def app (noir-middleware/app-handler [auth-routes 
                                       home-routes 
                                       upload-routes 
                                       gallery-routes
                                       app-routes]
                                      :middleware [wrap-restful-format]
                                      :access-rules [user-page]))

