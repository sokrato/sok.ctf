(ns io.github.sokrato.ctf.crack
  (:require
    [clojure.tools.logging :as log])
  (:import
    [io.github.sokrato.security HashCracker Hasher]
    [com.google.common.hash Hashing])
  (:gen-class))


(defn crack [{:keys [method prefix target max-len]
              :or   {method  "sha256"
                     prefix  ""
                     target  "000000"
                     max-len "6"}
              :as   opts}]
  (log/info "cracking" {:method  method
                        :prefix  prefix
                        :target  target
                        :max-len max-len})
  (let [hf (case method
             "sha256" (Hashing/sha256)
             "md5" (Hashing/md5)
             (throw (IllegalArgumentException. (str "unsupported method: " method))))
        ck (HashCracker. (Hasher/fromHashFunction hf))
        max-len (Integer/parseInt max-len)]
    (.crack ck prefix target max-len)))
