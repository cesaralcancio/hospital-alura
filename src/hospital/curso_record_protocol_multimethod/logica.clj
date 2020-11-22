(ns hospital.curso_record_protocol_multimethod.logica
  (:require [hospital.curso_record_protocol_multimethod.model :as h.v2.model]))

(defn agora []
  (h.v2.model/to-ms (java.util.Date.)))