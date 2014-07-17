;(ns picture-gallery.models.db
;  (:require [clojure.java.jdbc :as sql]
;            [environ.core :refer [env]]))
;
;(def db
;  {:subprotocol "postgresql"
;   :subname (env :db-url)  ; "//localhost/gallery"
;   :user (env :db-user)  ; "postgres"
;   :password (env :db-pass)   ;"postgres"
;   })
;
;(defmacro with-db [f & body]
;  `(sql/with-connection ~db (~f ~@body)))
;
;(defn create-user [user]
;  (with-db sql/insert-record :users user))
;
;(defn get-user [id]
;  (with-db sql/with-query-results
;    res ["select * from users where id = ?" id] (first res)))
;
;(defn add-image [userid name]
;  (with-db
;    sql/transaction
;    (if (sql/with-query-results
;          res
;          ["select userid from images where userid = ? and name = ?" userid name]
;          (empty? res))
;      (sql/insert-record :images {:userid userid :name name})
;      (throw
;        (Exception. "you have already uploaded an image with the same name")))))
;
;(defn images-by-user [userid]
;  (with-db
;    sql/with-query-results
;    res ["select * from images where userid = ?" userid] (doall res)))
;
;(defn get-gallery-previews []
;  (with-db
;    sql/with-query-results
;    res
;    ["select * from 
;      (select *, row_number() over (partition by userid) as row_number from images) 
;      as rows where row_number = 1"]
;    (doall res)))
;
;(defn delete-image [userid name]
;  (with-db
;    sql/delete-rows :images ["userid=? and name=?" userid name]))
;
;(defn delete-user [userid]
;  (with-db sql/delete-rows :users ["id=?" userid]))

(ns picture-gallery.models.db
  (:require [clojure.java.jdbc :as sql]
            [korma.db :refer [defdb transaction]]
            [korma.core :refer :all]))

(def db 
  {:subprotocol "postgresql"
   :subname "//localhost/gallery"
   :user "postgres"
   :password "postgres"})

(defdb korma-db db)

(defentity users)

(defentity images)

(defn create-user [user]
  (insert users (values user)))

(defn get-user [id]
  (first (select users
                 (where {:id id})
                 (limit 1))))
                 
(defn delete-user [id]
  (delete users (where {:id id})))  

(defn add-image [userid name]  
  (transaction
    (if (empty? (select images 
                        (where {:userid userid :name name})
                        (limit 1)))
      (insert images (values {:userid userid :name name}))
      (throw 
        (Exception. "you have already uploaded an image with the same name")))))
                           
(defn images-by-user [userid]
  (select images (where {:userid userid})))    
(defn delete-image [userid name]
  (delete images (where {:userid userid :name name}))) 

(defn get-gallery-previews []
  (exec-raw
    ["select * from 
     (select *, row_number() over (partition by userid) as row_number from images) 
     as rows where row_number = 1" []]
     :results)) 
