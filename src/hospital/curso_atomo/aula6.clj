(ns hospital.curso_atomo.aula6
  (:use [clojure pprint])
  (:require [hospital.curso_atomo.model :as h.model]))


(defn cabe-na-fila? [fila]
  (-> fila
      (count ,,,)
      (< ,,, 5)))

(defn chega-em
  [fila pessoa]
  (if
    (cabe-na-fila? fila)
    (conj fila pessoa)
    (do
      (println "Erro ao inserir pessoa" pessoa)
      (throw (ex-info "Full queue" {:tentando-adicionar pessoa})))))

(defn chega-em!
  "troca de referencia via ref-set"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (ref-set fila (chega-em @fila pessoa))))

(defn chega-em!
  "troca de referencia via alter"
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (alter fila chega-em pessoa)))

(defn simula-um-dia []
  (let [hospital {
                  :espera (ref h.model/empty-queue)
                  :lab1 (ref h.model/empty-queue)
                  :lab2 (ref h.model/empty-queue)
                  :lab3 (ref h.model/empty-queue)
                  }]
    (dosync
      (chega-em! hospital "cesar")
      (chega-em! hospital "paula")
      (chega-em! hospital "chico")
      (chega-em! hospital "bento")
      (chega-em! hospital "niva")
      ;(chega-em! hospital "felipe")
      ;(chega-em! hospital "david")
      )
    (pprint hospital)))

; (simula-um-dia)

(defn async-chega-em! [hospital pessoa]
  (future
    (Thread/sleep (rand 5000))
    (dosync
      (println "Tentando codigo sincronizado para essa pessoa" pessoa)
      (chega-em! hospital pessoa))))

(defn simula-um-dia-async []
  (let [hospital {
                  :espera (ref h.model/empty-queue)
                  :lab1 (ref h.model/empty-queue)
                  :lab2 (ref h.model/empty-queue)
                  :lab3 (ref h.model/empty-queue)
                  }
        futures (mapv #(async-chega-em! hospital %) (range 10))]

    ; (dotimes [pessoas 10] (async-chega-em! hospital pessoas))

    (dotimes [n 10]
      (pprint "inicio")
      (Thread/sleep 1000)
      (pprint hospital)
      (println futures)
      (pprint "fim"))
    ))

(simula-um-dia-async)