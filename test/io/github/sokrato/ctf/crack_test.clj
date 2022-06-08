(ns io.github.sokrato.ctf.crack-test
  (:require
    [clojure.test :refer :all]
    [io.github.sokrato.ctf.crack :refer :all])
  (:gen-class))

(deftest test-crack
  (is (= "010" (sha256 "5c4a4e" "abc123" "")))
  (is (= "011" (md5 "84eb13")))
  ;;
  )
