(ns hospital.curso-testes.model
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(defn novo-hospital []
  {:espera       empty-queue
   :laboratorio1 empty-queue
   :laboratorio2 empty-queue
   :laboratorio3 empty-queue}
  )

(s/def PacienteID s/Str)
(s/def Departamento (s/queue PacienteID))
(s/def Hospital {s/Keyword Departamento})

; validando meu modelo
(s/validate PacienteID "Cesar Augusto")
; (s/validate PacienteID nil)
; (s/validate Departamento ["Cesar Augusto"])
(s/validate Departamento (conj empty-queue "Cesar Augusto"))
(s/validate Hospital {:espera (conj empty-queue "Cesar Augusto")})

(pprint "Model set up ok")