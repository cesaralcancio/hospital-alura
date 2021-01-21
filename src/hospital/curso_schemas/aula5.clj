(ns hospital.curso_schemas.aula5
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'nao-eh-inteiro-positivo))
(def Plano [s/Keyword])
(def Paciente
  {:id                          PosInt
   :nome                        s/Str
   :plano                       Plano
   (s/optional-key :nascimento) s/Str})

(def Pacientes {PosInt Paciente})
(def Visitas {PosInt [s/Str]})

(s/defn adicion-paciente :- Pacientes
  [pacientes :- Pacientes paciente :- Paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente nao possui id" {:paciente paciente}))))

(s/defn adiciona-visitas :- Visitas
  [visitas :- Visitas paciente :- PosInt novas-visitas :- [s/Str]]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(s/defn imprimi-relatorio-paciente
  [visitas :- Visitas paciente :- PosInt]
  (println "Visitas do paciente" paciente "sao" (get visitas paciente)))


(defn teste-uso-de-pacientes []
  (let [guilherme {:id 123 :nome "Guilherme" :plano []}
        cesar {:id 456 :nome "Cesar" :plano []}
        paula {:id 789 :nome "Paula" :plano []}
        ; using reduce
        pacientes (reduce adicion-paciente {} [guilherme, cesar, paula])
        ; using shadowing
        visitas {}
        visitas (adiciona-visitas visitas 123 ["01/02/2019"])
        visitas (adiciona-visitas visitas 456 ["01/02/2019", "01/03/2019"])
        visitas (adiciona-visitas visitas 123 ["01/04/2019"])]

    (imprimi-relatorio-paciente visitas 123)
    (pprint pacientes)
    (pprint visitas)
    ))

(teste-uso-de-pacientes)


; Teste si postint aceita null
(def Teste {:id PosInt})
(s/validate teste nil)
