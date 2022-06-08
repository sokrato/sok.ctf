(ns io.github.sokrato.ctf.crack
  (:require
    ; [clojure.tools.logging :as log]
    [clj-commons.byte-streams :as bs]
    [io.github.sokrato.ctf.base64 :as b64]
    [clojure.string :as str])
  (:import
    (io.github.sokrato.util ByteHasher HmacSha256Hasher HashCracker)
    (clojure.lang Symbol)
    (com.google.common.io BaseEncoding))
  (:gen-class))


(def EmptyByteArray (.getBytes ""))

(defn b16-decode [^String s]
  (if (empty? s)
    EmptyByteArray
    (.decode (BaseEncoding/base16) (str/upper-case s))))

(defn crack' [^ByteHasher hf target prefix suffix]
  (let [r (-> (.crack
                (HashCracker/shared)
                hf
                prefix
                suffix
                target)
              bs/to-string)]
    (.substring
      r
      (count prefix)
      (- (count r) (count suffix)))))

(defmacro cracker [^Symbol name ^ByteHasher hf]
  `(defn ~name
     ([^String target#]
      (~name target# "" ""))
     ([^String target# ^String prefix# ^String suffix#]
      (crack'
        ~hf
        (b16-decode target#)
        (.getBytes prefix#)
        (.getBytes suffix#)))))

(cracker md5 ByteHasher/md5)

(cracker sha256 ByteHasher/sha256)

(defn hmacSha256 [^bytes payload ^bytes target]
  (crack'
    (HmacSha256Hasher. payload)
    target
    EmptyByteArray
    EmptyByteArray))

(defn jwt [^String token]
  (let [[header payload sig] (str/split token #"\.")
        p (format "%s.%s" header payload)]
    (hmacSha256
      (.getBytes p)
      (b64/decode sig))))
