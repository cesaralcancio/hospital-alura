(ns hospital.curso_schemas.aula4
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'nao-eh-inteiro-positivo-neh))
(def Plano [s/Keyword])
(def Paciente
  {:id                          PosInt
   :nome                        s/Str
   :plano                       Plano
   (s/optional-key :nascimento) s/Str})

(s/validate Paciente {:id 123 :nome "Cesar" :plano [:bradesco-saude :itau]})
(s/validate Paciente {:id 123 :nome "Cesar" :plano [:bradesco-saude]})
(s/validate Paciente {:id 123 :nome "Cesar" :plano []})
(s/validate Paciente {:id 123 :nome "Cesar" :plano nil})

; Esse eh um outro tipo de mapas de chave e valor
; Pacientes {14 PacienteA, 15 Paciente B}
(def Pacientes {PosInt Paciente})
(s/validate Pacientes {})
(s/validate Pacientes {15 {:id 123 :nome "Cesar" :plano []}})


