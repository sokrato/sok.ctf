(ns user
  (:require
    [clojure.string :as str]
    [clojure.java.io :as io]
    [clojure.tools.logging :as log]
    [cheshire.core :as json]
    [manifold.deferred :as d]
    [clj-commons.byte-streams :as bs]
    [aleph.http :as http]
    [io.github.sokrato.ctf.base64 :as b64]
    [io.github.sokrato.ctf.crack :as crack]
    [clj-commons.digest :as digest])
  (:import
    (java.nio.charset StandardCharsets)
    (java.net URLEncoder URLDecoder)
    (clojure.lang IDeref))
  (:gen-class))

(defn http-post [url data]
  @(http/post url {:body    (json/encode data)
                   :headers {"Cookie" "PHPSESSID=19077d36b3cb3989d050b36b63790692"}
                   :as      :text
                   }))

(defn json-decode [x]
  (json/decode x keyword))

(defn url-decode [^String s]
  (URLDecoder/decode s StandardCharsets/UTF_8))

(defn url-encode [^String s]
  (URLEncoder/encode s StandardCharsets/UTF_8))

;my baidu server:
;http://106.13.49.30/

(defn unwrap
  "deref the arg or get the exception within"
  [^IDeref der]
  (try
    (deref der)
    (catch Throwable exc exc)))
