(ns io.github.sokrato.ctf.text
  (:gen-class))

(def digits "0123456789")
(def lowercase-ascii "abcdefghijklmnopqrstuvwxyz")
(def uppercase-ascii "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
(def alphabet (str lowercase-ascii uppercase-ascii))
(def alpha-num (str alphabet digits))
(def punk "!\"#$%&'()*+,-./:;<=>?@[\\]^_")
(def alpha-num-punk (str alpha-num punk))