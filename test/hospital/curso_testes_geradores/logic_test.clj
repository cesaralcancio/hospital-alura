(ns hospital.curso-testes-geradores.logic-test
  (:require [clojure.test :refer :all]
            [hospital.curso-testes-geradores.logic :refer :all]
            [hospital.curso-testes-geradores.model :refer :all]
            [clojure.test.check.generators :as gen]
            [schema.core :as s]))

(s/set-fn-validation! true)

(deftest cabe-na-fila?-teste
  (testing "Que cabe na fila"
    (is (cabe-na-fila? {:espera []} :espera)))

  (testing "Que nao cabe na fila quando esta cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  (testing "Que nao cabe na fila quando esta mais que cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5 6]} :espera))))

  (testing "Que a fila eh null"
    (is (not (cabe-na-fila? {:espera [1 2 3]} :raio-x)))
    ))



(gen/sample gen/boolean 3)
(gen/sample gen/int)
(gen/sample gen/string)
(gen/sample gen/string-alphanumeric)
(gen/sample (gen/vector gen/small-integer 10))

