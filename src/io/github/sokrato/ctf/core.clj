(ns io.github.sokrato.ctf.core
  (:require
    [clojure.tools.logging :as log]
    [clojure.string :as str]
    [io.github.sokrato.ctf.crack :refer [crack]])
  (:gen-class))

(defn help []
  (binding [*out* *err*]
    (println "Usage:\n"
             "   method=(sha256|md5) [max-len=6 prefix= target=000000]")))

(defn- parse-kv [s]
  (let [[k v] (str/split s #"=")
        k (keyword k)
        v (or v "")]
    [k v]))

(defn parse-args [args]
  (->>
    (map parse-kv args)
    (into {})))

(defn -main [& args]
  (if (empty? args)
    (help)
    (-> args
        parse-args
        crack
        println)))
