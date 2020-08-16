(ns hospital.v2.aula2
  (:use clojure.pprint))

; Paciente Plano de Saude ===> + plano de saude
; Paciente Particular ===> + 0

; heranca com problema de tipos 2^n
; Pesquisar porque nao usar heranca para reaproveitar alguns campos
; (defrecord PacientePlanoSaude HERDA Paciente [plano])

(defrecord Paciente [id nome])
(defrecord PacienteParticular [id nome])
(defrecord PacientePlano [id nome plano])

(defprotocol Cobravel
  (dev-assinar-pre-autorizacao? [paciente procedimento valor]))

(extend-type PacienteParticular
  Cobravel
  (dev-assinar-pre-autorizacao? [paciente procedimento valor]
    (>= valor 50)))

(extend-type PacientePlano
  Cobravel
  (dev-assinar-pre-autorizacao? [paciente procedimento valor]
    (let [plano (get paciente :plano)]
      (not (some #(= % procedimento) plano)))))
; contains? => verifica o indice e o indice voce fica dependente da estrutura

(let [particular (PacienteParticular. 15 "Cesar")
      plano (PacientePlano. 20 "Paula" [:raio-x :ultrasom])]
  (pprint (dev-assinar-pre-autorizacao? particular :raio-x 60))
  (pprint (dev-assinar-pre-autorizacao? particular :raio-x 40))
  (pprint (dev-assinar-pre-autorizacao? plano :raio-x 500))
  (pprint (dev-assinar-pre-autorizacao? plano :ultrasom 500))
  (pprint (dev-assinar-pre-autorizacao? plano :terapia 500)))