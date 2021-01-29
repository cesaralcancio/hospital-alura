(ns hospital.curso-testes-geradores.logic-test
  (:use clojure.pprint)
  (:require [clojure.test :refer :all]
            [hospital.curso-testes-geradores.logic :refer :all]
            [hospital.curso-testes-geradores.model :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.clojure-test :refer (defspec)]
            [clojure.test.check.properties :as prop]
            [schema.core :as s]
            [schema-generators.generators :as g]))

;(gen/sample gen/boolean 3)
;(gen/sample gen/int)
;(gen/sample gen/string)
;(gen/sample gen/string-alphanumeric)
;(gen/sample (gen/vector gen/small-integer 10))

;(pprint (g/sample 10 PacienteID))
;(pprint (g/sample 10 Departamento))
;(pprint (g/sample 10 Hospital))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-teste
  (testing "Que cabe na fila"
    (is (cabe-na-fila? {:espera []} :espera)))

  (testing "Que cabe pessoas em filas de tamanho ate 4 inclusive"
    (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4))]
      (is (cabe-na-fila? {:espera fila} :espera))))

  (testing "Que nao cabe na fila quando esta cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  (testing "Que nao cabe na fila quando esta mais que cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))

  (testing "Que a fila eh null"
    (is (not (cabe-na-fila? {:espera [1 2 3]} :raio-x)))
    ))

; doseq multiplica os dados
;(deftest chega-em-test
;  (testing "Que é colocado uma pessoa em fila menores que 5")
;  (doseq [fila (gen/sample (gen/vector gen/string-alphanumeric 0 4) 10)
;          pessoa (gen/sample gen/string-alphanumeric)]
;    (println pessoa fila)
;    )
;  )

; o teste a seguir é generativo e funciona
; mas.... o resultado dele parece MUITO uma cópia do nosso código implementado
; se eu coloco um bug la eu provavelmente coloco um bug aqui
(defspec coloca-uma-pessoa-em-filas-menores-que-5 10
         (prop/for-all [fila (gen/vector gen/string-alphanumeric 0 4)
                        pessoa gen/string-alphanumeric]
                       (is (= {:espera (conj fila pessoa)}
                              (chega-em {:espera fila} :espera pessoa)))))

; (clojure.string/join ["a" "b" "c"])

(def nome-aleatorio-gen
  (gen/fmap clojure.string/join
            (gen/vector gen/char-alphanumeric 5 10)))

(defn transforma-vetor-em-fila [vetor]
  (reduce conj empty-queue vetor)
  )

(def fila-nao-cheia-gen
  (gen/fmap transforma-vetor-em-fila
            (gen/vector nome-aleatorio-gen 0 4)))

(def fla-de-espera-gen
  (gen/fmap transforma-vetor-em-fila
            (gen/vector nome-aleatorio-gen 0 50)))

(defn total-de-pacientes [hospital]
  (reduce + (map count (vals hospital)))
  )

(defn transfere-ignorando-erro [hospital para]
  (try
    (let [novo-hospital (transfere hospital :espera para)]
      ;(println "transferido com sucesso para " para)
      novo-hospital
      )
    (catch Exception e
      ;(println "Exception: " (ex-data e))
      (cond
        (= :full-queue (-> e ex-data :type)) hospital
        :else (throw e)
        )
      hospital
      )))

(defspec
  transfere-tem-que-manter-a-quantidade-de-pessoa 10
  (prop/for-all
    [espera fla-de-espera-gen
     raio-x fila-nao-cheia-gen
     ultrasom fila-nao-cheia-gen
     vai-para (gen/vector (gen/elements [:raio-x :ultrasom]) 0 20)]

    ;(println (count espera))
    ;(println (count raio-x))
    ;(println (count ultrasom))
    ;(println (count vai-para))

    (let [hospital-inicial {:espera espera :raio-x raio-x :ultrasom ultrasom}
          hospital-final (reduce transfere-ignorando-erro hospital-inicial vai-para)]
      (is (= (total-de-pacientes hospital-inicial)
             (total-de-pacientes hospital-final)))
      )
    )
  )



(defn adiciona-fila-de-espera [[hospital fila]]
  (assoc hospital :espera fila))

(def hospital-gen
  (gen/fmap adiciona-fila-de-espera
            (gen/tuple (gen/not-empty (g/generator Hospital))
                       fila-nao-cheia-gen)))

(def chega-em-gen
  "Gerador de chegadas no hospital"
  (gen/tuple (gen/return chega-em)
             (gen/return :espera)
             nome-aleatorio-gen))

(defn transfere-gen [hospital]
  "Gerador de transferencias no hospital"
  (let [departamentos (keys hospital)]
    (gen/tuple (gen/return transfere)
               (gen/elements departamentos)
               (gen/elements departamentos))))

(defn acao-gen [hospital]
  (gen/one-of [chega-em-gen
               (transfere-gen hospital)]))

(defn acoes-gen [hospital]
  (gen/not-empty (gen/vector (acao-gen hospital) 1 100)))

(defspec
  simula-um-dia-do-hospital-nao-perde-pessoas 10
  (prop/for-all [hospital hospital-gen]
                (let [acoes (gen/sample (acoes-gen hospital) 1)]
                  (println acoes)
                  true)
                ))
