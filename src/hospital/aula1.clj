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
  (println  "Classe hospital" (class hospital)))

(simula-um-dia)