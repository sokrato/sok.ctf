(ns io.github.sokrato.ctf.base64
  (:require
    [clj-commons.byte-streams :as bs])
  (:import
    (java.util Base64))
  (:gen-class))

(def ^:const byte-array-type (type (.getBytes "")))

(defmulti encode type)
(defmethod encode byte-array-type [^bytes x]
  (.encode (Base64/getEncoder) x))
(defmethod encode String [^String x]
  (.encode (Base64/getEncoder) (.getBytes x)))


(defmulti decode type)
(defmethod decode byte-array-type [^bytes x]
  (.decode (Base64/getDecoder) x))
(defmethod decode String [^String x]
  (.decode (Base64/getDecoder) (.getBytes x)))


(defmulti url-encode type)
(defmethod url-encode byte-array-type [^bytes x]
  (.encode (Base64/getUrlEncoder) x))
(defmethod url-encode String [^String x]
  (.encode (Base64/getUrlEncoder) (.getBytes x)))


(defmulti url-decode type)
(defmethod url-decode byte-array-type [^bytes x]
  (.decode (Base64/getUrlDecoder) x))
(defmethod url-decode String [^String x]
  (.decode (Base64/getUrlDecoder) (.getBytes x)))


(defn encode-as-str [x]
  (-> (encode x)
      bs/to-string))

(defn decode-as-str [x]
  (-> (decode x)
      bs/to-string))

(defn url-encode-as-str [x]
  (-> (url-encode x)
      bs/to-string))

(defn url-decode-as-str [x]
  (-> (url-decode x)
      bs/to-string))