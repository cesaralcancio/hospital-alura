(ns hospital.aula1
  (:use [clojure pprint])
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

(defn simula-um-dia []
  ; root binding
  (def hospital (h.model/novo-hospital))
  (def hospital (h.logic/chega-em hospital :espera "111"))
  (def hospital (h.logic/chega-em hospital :espera "222"))
  (def hospital (h.logic/chega-em hospital :espera "333"))
  ; funciona mas comecamos a ter um treco no coracao kkkk
  ; evitar variavel global e ficar mudando ela
  (println "Completo")
  (pprint hospital)

  (println "Chega")
  (def hospital (h.logic/chega-em hospital :laboratorio1 "444"))
  (def hospital (h.logic/chega-em hospital :laboratorio2 "555"))
  (pprint hospital)

  (println "Atende")
  (def hospital (h.logic/atende hospital :laboratorio1))
  (def hospital (h.logic/atende hospital :espera))
  (pprint hospital)

  (println "Chega")
  (def hospital (h.logic/chega-em hospital :espera "666"))
  (def hospital (h.logic/chega-em hospital :espera "777"))
  (def hospital (h.logic/chega-em hospital :espera "888"))
  (def hospital (h.logic/chega-em hospital :espera "999"))
  (def hospital (h.logic/chega-em hospital :espera "222"))
  (def hospital (h.logic/chega-em hospital :espera "333"))
  (pprint hospital))

; (simula-um-dia)
(println "Start...")

(defn chega-em-malvado [pessoa]
  (def hospital (h.logic/chega-em-pausado hospital :espera pessoa))
  (println "apos inserir" pessoa))

(defn imprime-hospital []
  (Thread/sleep 4000)
  (println "Imprime-hospital")
  (pprint hospital))

(defn simula-um-dia-em-paralelo
  []
  (def hospital (h.model/novo-hospital))
  (.start (Thread. #(chega-em-malvado "111")))
  (.start (Thread. #(chega-em-malvado "222")))
  (.start (Thread. #(chega-em-malvado "333")))
  (.start (Thread. #(chega-em-malvado "444")))
  (.start (Thread. #(chega-em-malvado "555")))
  (.start (Thread. #(chega-em-malvado "666")))
  (.start (Thread. #(imprime-hospital)))
  )

(simula-um-dia-em-paralelo)