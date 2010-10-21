(ns populate-db-clojure.core
  (:use [clojure.contrib.sql]
        [clojure.contrib.string :only [split]])
  (:import [java.io FileInputStream BufferedInputStream BufferedReader InputStreamReader File ByteArrayInputStream]
           [java.util.zip GZIPInputStream]
           [java.util Arrays]))


; Example command to insert data contained in a mnist gzipped file in Jeff's converted format
; into the database (along with supporting entries in the Source and DataSet tables:
;
; (use 'populate-db-clojure.core)
; (use 'clojure.contrib.sql)
;
; (with-connection db
;   (insert-rows "Handwriting.Source" [1 "MNIST" "http://yann.lecun.com/exdb/mnist/"]))
;
; (with-connection db
;   (insert-rows "Handwriting.DataSet" [1 1 "Training Set" "http://yann.lecun.com/exdb/mnist/train-images-idx3-ubyte.gz" "2010-10-15 00:00:00"]))
;
; (with-connection db
;   (do
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.0.a.txt.gz" 1 "0")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.1.a.txt.gz" 1 "1")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.2.a.txt.gz" 1 "2")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.3.a.txt.gz" 1 "3")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.4.a.txt.gz" 1 "4")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.5.a.txt.gz" 1 "5")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.6.a.txt.gz" 1 "6")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.7.a.txt.gz" 1 "7")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.8.a.txt.gz" 1 "8")
;     (sql-insert-characters-mnist "/home/ulman/CSI710/project/data/mnist.9.a.txt.gz" 1 "9")))
;
; Example command to select a individual data element from the database:
;
; (import 'java.util.Arrays)
; (with-connection db
;   (with-query-results rs ["SELECT * FROM Handwriting.Data WHERE Data.ixData = 1"]
;     (doall (map #(println % (Arrays/toString (signed-to-unsigned-array (:bdata %)))) rs))))

(defmacro dbg [x]
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

(defn signed-to-unsigned-array [signed-byte-array]
  "The clojure.contrib.sql driver returns blob data as a signed byte array. However, the data
   we are interested in is unsigned bytes. This function takes the returned byte array
   and returns an array of integer representations of the unsigned bytes."
  (let [size (alength signed-byte-array)
        new-array (int-array size)]
    (loop [i 0]
      (if (< i size)
        (let [old-val (aget signed-byte-array i)
              new-val (if (< old-val 0) (+ old-val 256) old-val)]
          (aset-int new-array i new-val)
          (recur (inc i)))))
    new-array))

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
  (vector nil ixDataSet sCharacter iRows iCols (ByteArrayInputStream. bData)))

(defn sql-insert-characters-mnist
  "Inserts all character data from the gzipped MNIST file and inserts it into the database.
   The id for the DataSet that the character data is part of and a string indicating the
   character or digit that the file contains must be indicated. Optionally, a sql batch insert
   size can be provided for efficiency."
  ([file-name ixDataSet sCharacter]
    (sql-insert-characters-mnist file-name ixDataSet sCharacter 500))
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

(defn get-db-data [ixData]
  (with-query-results rs ["SELECT * FROM Handwriting.Data WHERE Data.ixData = ?" ixData] (:bdata rs)))

(defn print-db-data [ixData]
  (with-connection db
    (let [rs (get-db-data ixData)]  
      (doall (map #(println % (Arrays/toString (signed-to-unsigned-array (:bdata %)))) rs)))))
