(ns io.github.sokrato.util.bytesgen-test
  (:require
    [clojure.tools.logging :as log]
    [clojure.test :refer :all])
  (:import
    [io.github.sokrato.util BytesGen])
  (:gen-class))

(defn gen->seq [^BytesGen gen]
  (loop [res []]
    (if-let [it (.orElse (.next gen) nil)]
      (recur (conj res (String. it)))
      res)))

(deftest BytesGen-test
  (let [g (BytesGen. (.getBytes "ab") 2)
        v (gen->seq g)]
    (is (= ["aa" "ab" "ba" "bb"] v))))
