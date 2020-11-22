(ns hospital.curso_record_protocol_multimethod.aula1
  (:use clojure.pprint))

(defn adiciona-paciente
  "Os pacientes ...."
  [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Problema ao inserir null" {:paciente paciente}))))

(defn testa-uso-pacientes []
  (let [pacientes {}
        guilherme {:id 15 :nome "Guilherme" :nascimento "09/05/1991"}
        daniela {:id 1 :nome "Daniela" :nascimento "02/10/1989"}
        pau {:nome "Paula" :nascimento "02/10/2020"}]
    (pprint (adiciona-paciente pacientes guilherme))
    (pprint (adiciona-paciente pacientes daniela))
    (pprint (adiciona-paciente pacientes pau))))

; (testa-uso-pacientes)

(defrecord Paciente [id nome nascimento])

(println (->Paciente 1 "Cesar" "1/5/1991"))
(pprint (->Paciente 1 "Cesar" "1/5/1991"))
(pprint (Paciente. 1 "Cesar" "1/5/1991"))
(pprint (map->Paciente {:id 1 :nome "Cesar" :nascimento "1/5/1991"}))

(let [cesar (Paciente. 1 "Cesar" "1/5/1991")]
  (pprint "Ola")
  (pprint (vals cesar))
  (pprint (record? cesar))
  )

(pprint (assoc (Paciente. nil "Cesar" "1/5/1991") :id 38))
(pprint (class (assoc (Paciente. nil "Cesar" "1/5/1991") :id 38)))