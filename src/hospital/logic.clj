(ns hospital.logic)

(defn cabe-na-fila? [hospital departamento]
  (-> hospital
      (get ,,, departamento)
      (count ,,,)
      (< ,,, 5)))

(defn chega-em [hospital departamento pessoa]
    (if (cabe-na-fila? hospital departamento)
      (update hospital departamento conj pessoa)
      (throw (ex-info "Queue is full" {:trying-to-add pessoa})))
  )

(defn chega-em-pausado [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (do (Thread/sleep (* (rand) 1000))
        (update hospital departamento conj pessoa))
    (throw (ex-info "Queue is full" {:trying-to-add pessoa})))
  )

(defn chega-em-pausado-logando
  [hospital departamento pessoa]
  (println "1 - Tentando adicionar a pessoa" pessoa)
  (Thread/sleep (* (rand) 1000))
  (if (cabe-na-fila? hospital departamento)
    (do
      ; (Thread/sleep (* (rand) 1000))
        (println "2 - Dando o update" pessoa)
        (update hospital departamento conj pessoa))
    (throw (ex-info "Queue is full" {:trying-to-add pessoa})))
  )

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

(defn atende-completo
  [hospital departamento]
  {:paciente (update hospital departamento peek)
   :hospital (update hospital departamento pop)})

(defn atende-completo-que-chama-ambos
  [hospital departamento]
  (let [fila (get hospital departamento)
        peek-pop (juxt peek pop)
        [pessoa fila-atualizada] (peek-pop fila)
        hospital-atualizado (update hospital assoc departamento fila-atualizada)]
    {:paciente pessoa
     :hospital hospital-atualizado}))
