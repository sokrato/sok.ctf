(ns user
  ;; Alert! DO *NOT* require directly or indirectly clojure.tools.logging,
  ;; otherwise tools.logging is not AOT compiled.
  (:require
    [clojure.string :as str]
    [clojure.java.io :as io]
    [cheshire.core :as json])
  (:gen-class))

;; (println "to delve in: (g)")

(defn g []
  (require 'script.user)
  (in-ns 'script.user))
