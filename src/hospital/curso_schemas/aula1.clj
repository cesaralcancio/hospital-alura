(ns hospital.curso_schemas.aula1
  (:use clojure.pprint)
  (:require [schema.core :as s])
  )

(defn adicion-paciente [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente nao possui id" {:paciente paciente}))))

(pprint (adicion-paciente {} {:id 123 :nome "Cesar"}))
(pprint (adicion-paciente {} {:id 123 :nome "Guilherme"}))

(defn adiciona-visitas
  [visitas paciente novas-visitas]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(defn imprimi-relatorio-paciente [visitas paciente]
  (println "Visitas do paciente" paciente "sao" (get visitas paciente)))

(s/defn imprimi-relatorio-paciente [visitas paciente :- Long]
  (println "Visitas do paciente" paciente "sao" (get visitas paciente)))

(defn teste-uso-de-pacientes []
  (let [guilherme {:id 123 :nome "Guilherme"}
        cesar {:id 456 :nome "Cesar"}
        paula {:id 789 :nome "Paula"}
        ; using reduce
        pacientes (reduce adicion-paciente {} [guilherme, cesar, paula])
        ; using shadowing
        visitas {}
        visitas (adiciona-visitas visitas 123 ["01/02/2019"])
        visitas (adiciona-visitas visitas 456 ["01/02/2019", "01/03/2019"])
        visitas (adiciona-visitas visitas 123 ["01/04/2019"])]

    ; problema pois em alguns metodos paciente eh
    ; o ID do paciente e em outros eh o objeto paciente
    ; ou seja, mesmo simbolo com significados diferentes
    (imprimi-relatorio-paciente visitas guilherme)
    (pprint pacientes)
    (pprint visitas)
    ))

(teste-uso-de-pacientes)


; testando o schema
(s/validate Long 123)
(s/validate String "123")
(s/set-fn-validation! true)

(s/defn teste-simples [x :- Long]
  (println x))

(teste-simples 10)
(teste-simples "Guilherme")


(s/defn novo-paciente
  [id :- Long nome :- String]
  {:id id :nome nome})

(novo-paciente 123 "Cesar")
(novo-paciente "Cesar" 123)