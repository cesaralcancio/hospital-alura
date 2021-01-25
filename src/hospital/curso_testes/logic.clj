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

(defn atende [hospital departamento]
  (update hospital departamento pop))

(defn proximo-paciente
  "Retorna o proximo paciente"
  [hospital departamento]
  (-> hospital
      departamento
      peek))

(defn transfere
  "Transfere paciente da filas"
  [hospital de para]
  (let [pessoa (proximo-paciente hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))


(transfere {:espera [1] :raio-x []} :espera :raio-x)