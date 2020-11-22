(ns hospital.curso_record_protocol_multimethod.aula3
  (:use clojure.pprint)
  (:require [hospital.curso_record_protocol_multimethod.logica :as h.v2.logica]))

(defn carrega-paciente [id]
  (println "Carregando" id)
  (Thread/sleep 1000)
  {:id id, :carregado-em (h.v2.logica/agora)})

;(pprint (carrega-paciente 15))
;(pprint (carrega-paciente 30))


(defn carrega-se-na-existe
  [pacientes id carregadora]
  (if (contains? pacientes id)
    pacientes
    (let [paciente (carregadora id)]
      (assoc pacientes id paciente))))

;(pprint (carrega-se-na-existe {} 15 carrega-paciente))
;(pprint (carrega-se-na-existe {15 {:id 15}} 15 carrega-paciente))

(defprotocol Carregavel
  (carrega! [this id]))

(defrecord Cache
  [cache carregadora]

  Carregavel
  (carrega! [this id]
    (swap! cache carrega-se-na-existe id carregadora)
    (get @cache id)))

(def pacientes (->Cache (atom {}), carrega-paciente))

; treinar un pouco de atom pra relembrar
(let [pessoa (atom {:nome "Cesar"})]
  (pprint (:nome @pessoa))
  (swap! pessoa assoc :nome "Jose")
  (pprint (:nome @pessoa)))

