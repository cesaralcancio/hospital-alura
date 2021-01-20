(ns hospital.curso_schemas.aula3
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'nao-eh-inteiro-positivo-neh))

(s/def Paciente
  {:id PosInt, :nome s/Str})

(s/defn novo-paciente :- Paciente
  [id :- PosInt
   nome :- s/Str]
  {:id id :nome nome}
  )

(novo-paciente 1 "Cesar")
; (novo-paciente -1 "Cesar")

(defn maior-ou-igual-a-zero [x] (>= x 0))
(def ValorFinanceiro (s/constrained s/Num maior-ou-igual-a-zero))

(def Pedido
  {:paciente Paciente
   :valor ValorFinanceiro
   :procedimento s/Keyword})

(s/defn novo-pedido :- Pedido
  [paciente :- Paciente valor :- ValorFinanceiro procedimento :- s/Keyword]
  {:paciente paciente :valor valor :procedimento procedimento}
  )

(novo-pedido (novo-paciente 1 "Cesar") 10 :raio-x)

(def Numeros [s/Num])
(s/validate Numeros [1 2 3])

; nil nao eh numero
(s/validate Numeros [1 2 nil])
; nil eh um vetor valido de numero
(s/validate Numeros nil)
(s/validate Numeros [])



(def Plano [s/Keyword])
(s/validate Plano nil)
(s/validate Plano [])
(s/validate Plano [:plano-top])

(def Paciente {:id PosInt :nome s/Str :plano Plano})

(s/validate Paciente {:id 123 :nome "Cesar" :plano [:bradesco-saude :itau]})
(s/validate Paciente {:id 123 :nome "Cesar" :plano [:bradesco-saude]})
(s/validate Paciente {:id 123 :nome "Cesar" :plano []})
(s/validate Paciente {:id 123 :nome "Cesar" :plano nil})


(def Pessoa {:id s/Num :nome s/Str})
(s/validate Pessoa {:id 123})




