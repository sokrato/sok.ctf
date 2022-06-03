(ns io.github.sokrato.ctf.crack-test
  (:require
    [clojure.test :refer :all]
    [io.github.sokrato.ctf.crack :refer :all])
  (:gen-class))

(deftest test-crack
  (is (= "010" (crack {:prefix "abc123"
                       :method "sha256"
                       :target "5c4a4e"})))
  (is (= "011" (crack {:target "84eb13"
                       :method "md5"})))
  ;;
  )
