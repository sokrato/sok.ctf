(ns io.github.sokrato.ctf.threads
  (:require
    [clojure.tools.logging :as log]
    [manifold.deferred :as d])
  (:import
    (java.util.concurrent BlockingQueue ArrayBlockingQueue CountDownLatch)
    (clojure.lang IFn Volatile))
  (:gen-class))

(defprotocol IWorkGroup
  (stop [this] "stop this group")
  (running [this] "is it running?")
  (join [this] "wait for the tasks to be finished")
  (add [this task] "add a task")
  (add-all [this tasks] "add all tasks"))

(defrecord WorkGroup [^BlockingQueue q ^long concurrency ^Volatile running ^CountDownLatch latch]
  IWorkGroup
  (stop [this]
    (.clear q)
    (vreset! running false)
    (dotimes [_ concurrency]
      (.put q ::done)))
  (running [this] @running)
  (join [this] (.await latch))
  (add [this task] (if @running (future (.put q task))))
  (add-all [this tasks]
    (future
      (loop [tasks tasks]
        (if (and (not (empty? tasks))
                 @running)
          (do
            (.put q (first tasks))
            (recur (rest tasks)))
          )))))

(defn num-processor []
  (-> (Runtime/getRuntime) .availableProcessors))

(defn ^WorkGroup workgroup
  ([^IFn f]
   (workgroup f (max 4 (num-processor))))
  ([^IFn f ^long concurrency]
   (let [q (ArrayBlockingQueue. (* 10 concurrency))
         r (volatile! true)
         l (CountDownLatch. concurrency)
         wg (->WorkGroup q concurrency r l)
         f (fn []
             (loop [arg (.take q)]
               (when (and (not= ::done arg) @r)
                 (try
                   (if (= ::done (f arg))
                     (stop wg))
                   (catch Exception ex
                     (log/error ex "worker exception")))
                 (recur (.take q))))
             (.countDown l)
             (log/info "worker exit."))]
     (dotimes [_ concurrency]
       (.start (Thread. ^IFn f)))
     wg)))
