;(ns picture-gallery.routes.home
;  (:require [compojure.core :refer :all]
;            [picture-gallery.views.layout :as layout]
;            [noir.session :as session]
;            [picture-gallery.routes.gallery :refer [show-galleries]]
;            ))
;
;(defn home []
;  (layout/common (show-galleries)))
;
;(defroutes home-routes
;  (GET "/" [] (home)))
;
;;;;;;;;
(ns picture-gallery.routes.home
  (:require [compojure.core :refer [defroutes GET]]
            [picture-gallery.views.layout :as layout]
            [picture-gallery.util :refer [thumb-prefix]]
            [picture-gallery.models.db :as db]
            [noir.session :as session]))

(defn home [] 
  (layout/render "home.html"
                 {:thumb-prefix thumb-prefix                  
                  :galleries    (db/get-gallery-previews)}))
(defroutes home-routes
  (GET "/" [] (home)))

