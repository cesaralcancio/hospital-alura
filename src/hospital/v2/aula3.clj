(ns hospital.v2.aula3
  (:use clojure.pprint)
  (:require [hospital.v2.logica :as h.v2.logica]))

(defn carrega-paciente [id]
  (println "Carregando" id)
  (Thread/sleep 2000)
  {:id id, :carregado-em (h.v2.logica/agora)})

(pprint (carrega-paciente 15))
(pprint (carrega-paciente 30))
