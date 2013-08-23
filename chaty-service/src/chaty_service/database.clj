(ns chaty_service.database
  (:gen-class)
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def server1-conn  {:pool {} :spec {:host "127.0.0.1" :port 6379}})

(def active-users "active-users")

(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn set-user [name user]
  (let [unique-name (gensym name)]
      (wcar* (car/set unique-name user))
  unique-name))


(defn append-active-users [unique-name user]
  (wcar* (car/hset active-users unique-name user)))

(defn fetc-active-user [name]
  (wcar*(car/hget active-users name)))

(defn get-user [id]
  (wcar* (car/get id)))

(defn subsribe-channel [channel]
  (wcar* (car/subscribe  channel)))

(defn create-new-channel [user channel]
 (car/with-new-pubsub-listener (:spec server1-conn)
    {channel (fn f1 [msg] (println "joined " msg))}
      (car/subscribe  channel "hellou")))

(defn publis-message [message channel user]
  (wcar* (car/publish channel (str user "." message) )))

(defn leave-channel [channel user]
  (wcar* (car/unsubscribe channel)))

