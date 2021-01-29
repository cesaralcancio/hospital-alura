(ns hospital.curso-testes-geradores.logic
  (:use clojure.pprint)
  (:require [hospital.curso-testes.model :as m]
            [schema.core :as s]))

(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5)))

(defn chega-em [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Nao cabe ninguem neste departamento."
                    {:type         :full-queue
                     :paciente     pessoa
                     :departamento departamento}))))

(s/defn atende :- m/Hospital
  [hospital :- m/Hospital, departamento :- s/Keyword]
  (update hospital departamento pop))

(s/defn proximo-paciente :- (s/maybe m/PacienteID)
  "Retorna o proximo paciente"
  [hospital :- m/Hospital departamento :- s/Keyword]
  (-> hospital
      departamento
      peek))

(s/defn transfere :- m/Hospital
  "Transfere paciente da filas"
  [hospital :- m/Hospital de :- s/Keyword para :- s/Keyword]
  {
   :pre  [(contains? hospital de) (contains? hospital para)]
   :post [
          (=
            (+ (count (get hospital de)) (count (get hospital para)))
            (+ (count (get % de)) (+ (count (get % para)) 0)))
          ]
   }
  (if-let [pessoa (proximo-paciente hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))
    hospital))

(pprint "Logic set up ok")