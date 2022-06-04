(ns script.user
  (:require
    [clojure.string :as str]
    [clojure.java.io :as io]
    [clojure.tools.logging :as log]
    [cheshire.core :as json]
    [manifold.deferred :as d]
    [clj-commons.byte-streams :as bs]
    [aleph.http :as http])
  (:import
    (java.nio.charset StandardCharsets))
  (:gen-class))

(defn http-post [url data]
  @(http/post url {:body    (json/encode data)
                   :headers {"Cookie" "PHPSESSID=19077d36b3cb3989d050b36b63790692"}
                   :as      :text
                   }))

(defn json-decode [x]
  (json/decode x keyword))

(defn url-decode [^String s]
  (java.net.URLDecoder/decode s StandardCharsets/UTF_8))
