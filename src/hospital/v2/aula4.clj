(ns hospital.v2.aula4
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

(class 134)

(defmulti dev-assinar-pre-autorizacao-multi? )