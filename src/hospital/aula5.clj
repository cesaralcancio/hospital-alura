(ns hospital.aula5
  (:use [clojure.pprint])
  (:require [hospital.logic :as h.logic]
            [hospital.model :as h.model]))

(defn fatorial [numero]
  (let [numero-menor (- numero 1)]
    (if (= numero 0)
      1
      (* numero (fatorial numero-menor))
      )))

; (println (if (= 0 1) 1 (* 1 2)))
(println (fatorial 10))

(defn chega-em! [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa))

(defn transfere! [hospital de para]
  (swap! hospital h.logic/transfere de para))

(defn simula-um-dia []
  (let [hospital (atom (h.model/novo-hospital))]
    (chega-em! hospital "Joao")
    (chega-em! hospital "Maria")
    (chega-em! hospital "Daniel")
    (chega-em! hospital "Guilherme")

    ; Remember the last class how to apply functions to atom
    (pprint (peek (get @hospital :espera)))
    (pprint (peek (get (deref hospital) :espera)))

    (println "Listo...")
    (transfere! hospital :espera :laboratorio1)
    (transfere! hospital :espera :laboratorio2)
    (transfere! hospital :espera :laboratorio2)
    (transfere! hospital :laboratorio2 :laboratorio3)
    (pprint hospital)
    )
  )

; (transfere! hospital :espera :laboratorio1)
(simula-um-dia)