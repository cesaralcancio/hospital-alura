(ns hospital.curso-testes.logic)

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
    (throw (ex-info "Nao cabe ninguem neste departamento." {:paciente pessoa}))))

; retorna IllegalStateException
(defn chega-em [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (IllegalStateException. "Nao cabe ninguem neste departamento."))))

; retorna nil
(defn chega-em [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)))
