(ns populate-db-clojure.core
  (:use [clojure.contrib.sql :as sql]
        [clojure.contrib.string :as string :only [split trim join]])
  (:import [java.io FileInputStream BufferedInputStream BufferedReader InputStreamReader File ByteArrayOutputStream]
           [java.util.zip GZIPInputStream]))

;
; Example command to insert data contained in a mnist gzipped file in Jeff's converted format
; into the database:
;
; (use 'clojure.contrib.sql)
; (with-connection db
;   (sql-insert-characters-mnist "/path/to/data/file.gz" 1 "a"))
;


(defmacro dbg
  "A debugging macro which prints the value of expressions which it wraps."
  `(let [x# ~x] (println '~x "=" x#) x#))

(def db-host "ec2-67-202-7-152.compute-1.amazonaws.com")
(def db-port 3306)
(def db-name "test")

(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname (str "//" db-host ":" db-port "/" db-name)
         :user "test"
         :password "csi710"})


(defn get-reader-gz [^String file-name]
  "Returns a BufferedReader for the given gzipped file name."
  (-> file-name File. FileInputStream. GZIPInputStream. InputStreamReader. BufferedReader.))

(defn unsigned-to-signed-byte [unsigned-byte]
  "Given an unsigned-byte (represented by a Java integer) on [0 255] returns
   a signed Java byte on [-128 127] whose binary representation, when interpreted
   as an unsigned byte, equals the original unsigned-byte."
  (byte (if (<= unsigned-byte 127) unsigned-byte (- unsigned-byte 256) )))

(defn read-character-string-mnist [^String line]
  (let [tokens (split #"[ ]+" line)]
    (byte-array (map #(unsigned-to-signed-byte (Integer/parseInt %)) tokens))))

(defn read-character-mnist [^BufferedReader in]
  "Returns a byte array containing data for a single character in Jeff's MNIST converted file format."
  (read-character-string-mnist (.readLine in)))

(defn get-sql-insert-values [ixDataSet sCharacter iRows iCols bData]
  "Returns a vector containing values for a single character to be inserted into the Handwriting.Data table."
  (vector nil ixDataSet sCharacter iRows iCols (ByteArrayOutputStream. bData)))

(defn sql-insert-characters-mnist
  "Inserts all character data from the gzipped MNIST file and inserts it into the database.
   The id for the DataSet that the character data is part of and a string indicating the
   character or digit that the file contains must be indicated. Optionally, a sql batch insert
   size can be provided for efficiency."
  ([file-name ixDataSet sCharacter]
    (insert-characters-mnist file-name ixDataSet sCharacter 500))
  ([file-name ixDataSet sCharacter batch-size]
    (with-open [^BufferedReader in (get-reader-gz file-name)]
      (let [rows (line-seq in)
            row-batches (partition batch-size rows)
            iRows 28
            iCols 28]
        (dorun
          (map (fn [row-batch] 
                 (apply insert-rows
                 "Handwriting.Data"
                 (map (fn [bData] (get-sql-insert-values ixDataSet sCharacter iRows iCols (read-character-string-mnist bData)))
                      row-batch)))
               row-batches ))))))

; connect to database and run a simple query
;(with-connection db
;  (with-query-results rs ["show tables"]
;    (dorun (map println rs))))

