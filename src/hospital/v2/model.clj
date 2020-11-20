(ns hospital.v2.model)

(defprotocol Dateable
  (to-ms [this]))

(extend-type java.lang.Number
  Dateable
  (to-ms [this] 5))

(extend-type java.util.Date
  Dateable
  (to-ms [this] (.getTime this)))

(extend-type java.util.Calendar
  Dateable
  (to-ms [this] (.getTimeInMillis this)))

(extend-type java.lang.String
  Dateable
  (to-ms [this] (.length this)))