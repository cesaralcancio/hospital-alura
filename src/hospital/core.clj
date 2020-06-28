(ns hospital.core
  (:use [clojure pprint])
  (:require [hospital.model :as h.model]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

; espera FILA DE ESPERA 3
; laboratorio1
; laboratorio2
; laboratorio3

(let [hospital-do-cesar
  (h.model/novo-hospital)]
  (pprint hospital-do-cesar))

(pprint h.model/empty-queue)