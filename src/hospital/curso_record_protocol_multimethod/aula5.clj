(ns hospital.curso_record_protocol_multimethod.aula5
  (:use clojure.pprint))

(defn tipo-de-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)]
    (cond (= :urgente situacao) :sempre-permite
          (contains? paciente :plano) :plano-de-saude
          :else :credito-minimo)))

(defmulti deve-assinar? tipo-de-autorizador)
(defmethod deve-assinar? :sempre-permite [pedido] false)
(defmethod deve-assinar? :plano-de-saude [pedido]
  (let [plano (:plano (:paciente pedido))
        procedimento (:procedimento pedido)
        funcao #(= % procedimento)]
    (not (some funcao plano))))

(defmethod deve-assinar? :credito-minimo [pedido]
  (>= (:valor pedido 0) 50))

(let [particular1 {:id 15, :nome "Guilherme", :nascimento "18/9/1981", :situacao :urgente}
      particular2 {:id 15, :nome "Guilherme", :nascimento "18/9/1981", :situacao :normal}
      plano {:id 15, :nome "Guilherme", :nascimento "18/9/1981", :situacao :normal, :plano [:raio-x :ultrasom :coleta-de-sangue]}]

  (pprint (tipo-de-autorizador {:paciente particular1 :valor 100 :procedimento :coleta-de-sangue}))
  (pprint (tipo-de-autorizador {:paciente particular2 :valor 100 :procedimento :coleta-de-sangue}))
  (pprint (tipo-de-autorizador {:paciente plano :valor 100 :procedimento :coleta-de-sangue}))

  (pprint (deve-assinar? {:paciente particular1 :valor 100 :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar? {:paciente particular2 :valor 100 :procedimento :coleta-de-sangue}))
  (pprint (deve-assinar? {:paciente plano :valor 100 :procedimento :extracao-dente})))

