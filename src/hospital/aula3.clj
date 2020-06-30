(ns hospital.aula3
  (:use [clojure pprint])
  (:require [hospital.logic :as h.logic]
            [hospital.model :as h.model]))

(def nome "Cesar")
(println nome)
(def nome 123)
(println nome)

(println "---")
(println "shadowing")
(let [nome "Cesar"]
  (println nome)
  (let [nome "Joao"]
    (println nome))
  (println nome))

(defn teste-atomo []
  (let [hospital-silveira (atom { :espera h.model/empty-queue })]
    (println hospital-silveira)
    (pprint hospital-silveira)
    (pprint (deref hospital-silveira))
    (pprint @hospital-silveira)

    ; nao eh assim que altera o atom (mutavel)
    (pprint (assoc @hospital-silveira :laboratorio1 h.model/empty-queue))
    (pprint @hospital-silveira)

    ; uma das maneiras de alterar conteudo dentro de um atomo
    (swap! hospital-silveira assoc :laboratorio1 h.model/empty-queue)
    (swap! hospital-silveira assoc :laboratorio2 h.model/empty-queue)
    (pprint @hospital-silveira)

    ; update tradicional imutavel
    (pprint (update @hospital-silveira :laboratorio1 conj "111"))
    (pprint @hospital-silveira)

    (swap! hospital-silveira update :laboratorio1 conj "111")
    (pprint hospital-silveira)))
(teste-atomo)

(println "Chega em malvado...")

(defn chega-em-malvado! [hospital pessoa]
  (swap! hospital h.logic/chega-em-pausado-logando :espera pessoa)
  (println "3 - Apos inserir" pessoa))

(defn imprime-hospital [hospital]
  (Thread/sleep 4000)
  (println "Imprime-hospital")
  (pprint hospital))

(defn simula-um-dia-em-paralelo-malvado []
  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. #(chega-em-malvado! hospital "111")))
    (.start (Thread. #(chega-em-malvado! hospital "222")))
    (.start (Thread. #(chega-em-malvado! hospital "333")))
    (.start (Thread. #(chega-em-malvado! hospital "444")))
    (.start (Thread. #(chega-em-malvado! hospital "555")))
    (.start (Thread. #(chega-em-malvado! hospital "666")))
    (.start (Thread. #(imprime-hospital hospital)))))

; (simula-um-dia-em-paralelo-malvado)

(println "----")

(defn chega-em! [hospital pessoa]
  (swap! hospital h.logic/chega-em :espera pessoa)
  (println "3 - Apos inserir" pessoa))

(defn simula-um-dia-em-paralelo []
  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. #(chega-em! hospital "111")))
    (.start (Thread. #(chega-em! hospital "222")))
    (.start (Thread. #(chega-em! hospital "333")))
    (.start (Thread. #(chega-em! hospital "444")))
    (.start (Thread. #(chega-em! hospital "555")))
    (.start (Thread. #(chega-em! hospital "666")))
    (.start (Thread. #(imprime-hospital hospital)))))

; (simula-um-dia-em-paralelo)

(println "----")
(let [nome (atom "Cesar")]
  (println @nome)
  (swap! nome str "Teste")
  (println @nome)
  (reset-vals! nome "Jose")
  (println @nome))