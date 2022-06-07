(ns io.github.sokrato.ctf.http
  (:require
    [clojure.string :as str]
    [clojure.java.io :as io]
    [clojure.tools.logging :as log]
    [cheshire.core :as json]
    [manifold.deferred :as d]
    [clj-commons.byte-streams :as bs]
    [aleph.http :as http])
  (:gen-class))

(defn wrap-logging [f]
  (fn [req]
    (log/info "request" req)
    (->
      (d/chain
        (f req)
        (fn [resp]
          (log/info "request OK" resp)
          resp))
      (d/catch
        Exception
        (fn [e]
          (let [data (ex-data e)
                msg (if data
                      (-> data :body bs/to-string)
                      (ex-message e))]
            (log/error "request failed" msg))
          e)))))
