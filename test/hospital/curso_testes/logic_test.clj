(ns hospital.curso-testes.logic-test
  (:require [clojure.test :refer :all]
            [hospital.curso-testes.logic :refer :all]))

; boundary tests
; exatamente na borda e one off: -1, +1, <=, >=, = .

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

(deftest outro
  (testing (is (= 1 1))))

(deftest chega-em-teste
  (let [hospital-cheio {:espera [45 65 78 23 56]}]
    (testing "aceita pessoas enquanto cabem na fila"
      (is (= {:espera [1 2 3 4 5]}
             (chega-em {:espera [1 2 3 4]} :espera 5)))
      (is (= {:espera [1 2 5]}
             (chega-em {:espera [1 2]} :espera 5)
             )))

    (testing "nao aceita quando ja esta cheio"
      ; retorna o mesmo hospital
      ;(is (= {:espera [45 65 78 23 56]}
      ;       (chega-em {:espera [45 65 78 23 56]} :espera 80)))

      ; retorna (throw ex-info)
      ;(is (thrown? clojure.lang.ExceptionInfo
      ;             (chega-em {:espera [45 65 78 23 56]} :espera 80)))

      ; outra maneira de testar, usando os dados da exception, melhor que a string
      ;(is (try
      ;      (chega-em {:espera [45 65 78 23 56]} :espera 80)
      ;      false
      ;      (catch clojure.lang.ExceptionInfo e
      ;        (= :impossivel-colocar-pessoa-na-fila (:tipo (ex-data e))))))

      ; retorna throw IllegalStateException.
      ;(is (thrown? IllegalStateException
      ;             (chega-em {:espera [45 65 78 23 56]} :espera 80)))

      ; retorna nil
      ; (is (nil? (chega-em {:espera [45 65 78 23 56]} :espera 80)))

      (is (= {:hospital hospital-cheio :resultado :error}
             (chega-em-retorna-mapa {:espera [45 65 78 23 56]} :espera 80)))





      )))