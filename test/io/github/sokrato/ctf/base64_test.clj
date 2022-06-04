(ns io.github.sokrato.ctf.base64-test
  (:require
    [clojure.test :refer :all]
    [clj-commons.byte-streams :as bs]
    [io.github.sokrato.ctf.base64 :refer :all])
  (:gen-class))

(def cases
  ["hello" "aGVsbG8="])

(deftest test-encode-decode
  (testing "default encoding/decoding"
    (doseq [[x y] (partition 2 cases)]
      (is (= y
             (-> (bs/to-byte-array x)
                 encode
                 bs/to-string)))
      (is (= y
             (-> (encode x)
                 bs/to-string)))
      (is (= x
             (-> (decode y)
                 bs/to-string)))
      (is (= x
             (-> (bs/to-byte-array y)
                 decode
                 bs/to-string)))
      (is (= y
             (encode-as-str x)))
      (is (= x
             (decode-as-str y)))
      ))

  (testing "url encoding/decoding"
    (doseq [[x y] (partition 2 cases)]
      (is (= y
             (-> (bs/to-byte-array x)
                 encode
                 bs/to-string)))
      (is (= y
             (-> (encode x)
                 bs/to-string)))
      (is (= x
             (-> (decode y)
                 bs/to-string)))
      (is (= x
             (-> (bs/to-byte-array y)
                 decode
                 bs/to-string)))
      (is (= y
             (url-encode-as-str x)))
      (is (= x
             (url-decode-as-str y)))
      ))
  )


