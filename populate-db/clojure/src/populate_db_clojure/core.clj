(ns populate-db-clojure.core
  (:use [clojure.contrib.sql]))

(def db-host "ec2-67-202-7-152.compute-1.amazonaws.com")
(def db-port 3306)
(def db-name "test")

(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname (str "//" db-host ":" db-port "/" db-name)
         :user "test"
         :password "csi710"})

(with-connection db
  (with-query-results rs ["show tables"]
    (dorun (map println rs))))

