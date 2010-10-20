(ns analyze-db-clojure.core
  (:use [populate-db-clojure.core]
        [clojure.contrib.sql]
        [clojure.contrib.string :only [split]])
  (:import [java.io FileInputStream BufferedInputStream BufferedReader InputStreamReader File ByteArrayInputStream]
           [java.util.zip GZIPInputStream]
           [java.util Arrays]))

;
; Example command to calculate mean intensities for each sample by character type
; and store the results back to the database
;
; (do
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "0")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "1")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "2")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "3")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "4")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "5")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "6")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "7")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "8")))
;   (populate-db-mean-intensity (deref (analyze-mean-intensity "9"))))
;
; The database can then be used to calculate the average MeanIntensity for each character:

; select Data.sCharacter, avg(Result.sValue) FROM Result, Data WHERE Data.ixData = Result.ixData and Result.sKey = "MeanIntensity" group by Data.sCharacter
; 
; character      mean intensity
;     0              44.20
;     1              19.43
;     2              37.95
;     3              36.13
;     4              31.05
;     5              32.95
;     6              35.25
;     7              29.24
;     8              38.44
;     9              31.43

(defn populate-db-mean-intensity [result-map]
  (with-connection db
    (apply insert-rows "Handwriting.Result" (map #(vector nil (first %) "MeanIntensity" (second %)) result-map))))

(defn calculate-mean-intensity [rs result-map]
  (let [ixData (:ixdata rs)
        bData  (signed-to-unsigned-array (:bdata rs))
        sum    (apply + bData)
        size   (alength bData)
        mean   (float (/ sum size))]
    (swap! result-map assoc ixData mean)))

(defn analyze-mean-intensity [character]
  (let [result-map (atom {})]
    (with-connection db
      (with-query-results rs ["SELECT * FROM Handwriting.Data WHERE Data.sCharacter = ?" character]
        (doall (map calculate-mean-intensity rs (repeat result-map)))))
    result-map))

