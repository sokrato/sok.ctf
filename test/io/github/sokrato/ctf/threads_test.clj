(ns io.github.sokrato.ctf.threads-test
  (:require
    [clojure.test :refer :all]
    [io.github.sokrato.ctf.threads :refer :all])
  (:import
    (java.util.concurrent.atomic AtomicInteger))
  (:gen-class))

(deftest test-workgroup
  (let [cnt (AtomicInteger.)
        wg (workgroup #(.addAndGet cnt %) 4)
        _ (add-all wg (range 10))
        _ (add wg 10)
        _ (add-all wg (repeat 4 :io.github.sokrato.ctf.threads/done))]
    (join wg)
    (is (= 55 (.intValue cnt)))))