(ns hospital.curso-testes.logic
  (:use clojure.pprint)
  (:require [hospital.curso-testes.model :as m]
            [schema.core :as s]))

(defn cabe-na-fila? [fila]
  (> (alength fila) 5))

(defn cabe-na-fila?
  [hospital departamento]
  (if-let [fila (get hospital departamento)]
    (-> fila
        count
        (< 5))))

(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
          departamento
          count
          (< 5)))

; retorna o mesmo hospital
(defn chega-em [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    hospital))

; retorna ex-info exception Clojure.lang.ExceptionInfo
(defn chega-em [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Nao cabe ninguem neste departamento."
                    {:paciente pessoa, :tipo :impossivel-colocar-pessoa-na-fila}))))

; retorna IllegalStateException
(defn chega-em [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (IllegalStateException. "Nao cabe ninguem neste departamento."))))

; retorna nil
(defn chega-em [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))

; retorna nil, funcao privada
(defn- tenta-colocar-na-fila
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))

(defn chega-em-retorna-mapa
  [hospital departamento pessoa]
  (if-let [novo-hospital (tenta-colocar-na-fila hospital departamento pessoa)]
    {:hospital novo-hospital, :resultado :sucesso}
    {:hospital hospital, :resultado :error}))

; copiar funcoes dos outros arquivos
; os chega-em anteriores nao funcionam
(defn chega-em [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Nao cabe ninguem neste departamento." {:paciente pessoa :departamento departamento}))))

(s/defn atende :- m/Hospital
  [hospital :- m/Hospital, departamento :- s/Keyword]
  (update hospital departamento pop))

(s/defn proximo-paciente :- m/PacienteID
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
  (let [pessoa (proximo-paciente hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))

(pprint "Logic set up ok")