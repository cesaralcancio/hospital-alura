(ns hospital.curso_schemas.aula2
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(s/defrecord Paciente [id :- Long nome :- s/Str])

(pprint (->Paciente 15 "Cesar"))
(pprint (Paciente. 15 "Cesar"))
(pprint (map->Paciente {15 "Cesar"}))
(pprint (map->Paciente {:id 15 :nome "Cesar"}))

(pprint (->Paciente "Cesar" 15))
(pprint (Paciente. "Cesar" 15))
(pprint (map->Paciente {:id "Cesar" :nome 15}))

(def PacienteSchema
  "Schema para o paciente"
  {:id s/Num :nome s/Str})

(s/explain PacienteSchema)
(s/validate PacienteSchema {:id 123 :nome "Cesar"})
(s/validate PacienteSchema {:id 123 :nome 123})
(s/validate PacienteSchema {:id 123 :name "Cesar"})
(s/validate PacienteSchema {:id 123 :nome "Jose" })


(s/defn novo-paciente :- PacienteSchema
  [id :- s/Num nome :- String]
  {:id id :nome nome})

(novo-paciente 15 "Cesar")

(defn estritamente-positivo? [x]
  (> x 0))

(def EstritamentoPositivo (s/pred estritamente-positivo?))

(pprint (s/validate EstritamentoPositivo 15))
(pprint (s/validate EstritamentoPositivo 0))
(pprint (s/validate EstritamentoPositivo -15))
(pprint (s/validate EstritamentoPositivo "Cesar"))



(def PacienteSchema
  "Schema para o paciente"
  {:id (s/constrained s/Int pos?) :nome s/Str})

(def PacienteSchema
  "Schema para o paciente"
  {:id (s/constrained s/Int #(> % 0) 'positivo) :nome s/Str})

(s/explain PacienteSchema)
(s/validate PacienteSchema {:id 123 :nome "Cesar"})
(s/validate PacienteSchema {:id 0 :nome "Cesar"})
