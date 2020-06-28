(ns hospital.model)

(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(defn novo-hospital []
  {:espera empty-queue
   :laboratorio1 empty-queue
   :laboratorio2 empty-queue
   :laboratorio3 empty-queue}
  )

