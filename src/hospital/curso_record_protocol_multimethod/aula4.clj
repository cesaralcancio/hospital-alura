(ns hospital.curso_record_protocol_multimethod.aula4
  (:use clojure.pprint))

(defrecord PacienteParticular [id, nome, nascimento, situacao])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, situacao, plano])

(defprotocol Cobravel
  (dev-assinar-pre-autorizacao? [paciente procedimento valor]))

(defn eh-urgente? [paciente]
  (= :urgente (:situacao paciente :normal)))

(extend-type PacienteParticular
  Cobravel
  (dev-assinar-pre-autorizacao? [paciente procedimento valor]
    (and (>= valor 50) (not (eh-urgente? paciente)))))

(extend-type PacientePlanoDeSaude
  Cobravel
  (dev-assinar-pre-autorizacao? [paciente procedimento valor]
    (let [plano (get paciente :plano)]
      (and (not (some #(= % procedimento) plano)) (not (eh-urgente? paciente))))))

(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :normal)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/9/1981", :normal, [:raio-x :ultrasom])]
  ;(pprint particular)
  (pprint (dev-assinar-pre-autorizacao? particular :raio-x 50))
  (pprint (dev-assinar-pre-autorizacao? particular :raio-x 49))
  ;(pprint plano)
  (pprint (dev-assinar-pre-autorizacao? plano :raio-x 60))
  (pprint (dev-assinar-pre-autorizacao? plano :sangue 60)))

(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/9/1981", :urgente, [:raio-x :ultrasom])]
  ;(pprint particular)
  (pprint (dev-assinar-pre-autorizacao? particular :raio-x 50))
  (pprint (dev-assinar-pre-autorizacao? particular :raio-x 49))
  ;(pprint plano)
  (pprint (dev-assinar-pre-autorizacao? plano :raio-x 60))
  (pprint (dev-assinar-pre-autorizacao? plano :sangue 60)))

; (class 134)

(defmulti dev-assinar-pre-autorizacao-multi? class)
(defmethod dev-assinar-pre-autorizacao-multi? PacienteParticular [paciente]
  (println "Paciente particular")
  true)
(defmethod dev-assinar-pre-autorizacao-multi? PacientePlanoDeSaude [paciente]
  (println "Paciente plano de saude")
  false)

(let [particular (->PacienteParticular 15, "Guilherme", "18/9/1981", :urgente)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/9/1981", :urgente, [:raio-x :ultrasom])]
  (pprint (dev-assinar-pre-autorizacao-multi? particular))
  (pprint (dev-assinar-pre-autorizacao-multi? plano)))

(println "-----")
(println "-----")

; explorando funcao que define a estrategia de um defmulti
(defrecord MeuObjeto [id, nome])

(defn minha-funcao [p]
  (let [content (:content p)
        id (:id p)]
    (class content)))

(defmulti multi-teste? minha-funcao)
(defmethod multi-teste? MeuObjeto [pedido]
  pedido)

(let [meuobjeto (->MeuObjeto 15 "Cesar")]
  (pprint "Ola")
  (pprint meuobjeto)
  (pprint (minha-funcao {:id 123 :content meuobjeto}))
  (pprint (multi-teste? {:id 123 :content meuobjeto})))
; fim exploracao

(println "-----")

(defn estrategia [pedido]
  (let [situacao (:situacao (:paciente pedido))
        paciente (:paciente pedido)]
    (if (= situacao :urgente)
      :sempre-permite
      (class paciente))))

(defmulti deve-cobrar? estrategia)
(defmethod deve-cobrar? :sempre-permite [pedido]
  false)
(defmethod deve-cobrar? PacienteParticular [pedido]
  (>= (:valor pedido) 50))
(defmethod deve-cobrar? PacientePlanoDeSaude [pedido]
  (let [plano (:plano (:paciente pedido))
        funcao #(= % (:procedimento pedido))]
    (not (some funcao plano))))

(let [particular1 (->PacienteParticular 15, "Guilherme", "18/9/1981", :urgente)
      particular2 (->PacienteParticular 15, "Guilherme", "18/9/1981", :normal)
      plano (->PacientePlanoDeSaude 15, "Guilherme", "18/9/1981", :normal, [:raio-x :ultrasom])]

  (pprint (estrategia {:paciente particular1 :valor 100 :procedimento :coleta-de-sangue}))
  (pprint (estrategia {:paciente particular2 :valor 100 :procedimento :coleta-de-sangue}))
  (pprint (estrategia {:paciente plano :valor 100 :procedimento :coleta-de-sangue}))

  (pprint (deve-cobrar? {:paciente particular1 :valor 100 :procedimento :coleta-de-sangue}))
  (pprint (deve-cobrar? {:paciente particular2 :valor 100 :procedimento :coleta-de-sangue}))
  (pprint (deve-cobrar? {:paciente plano :valor 100 :procedimento :coleta-de-sangue})))

