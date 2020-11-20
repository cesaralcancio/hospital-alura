(ns hospital.v2.logica
  (:require [hospital.v2.model :as h.v2.model]))

(defn agora []
  (h.v2.model/to-ms (java.util.Date.)))