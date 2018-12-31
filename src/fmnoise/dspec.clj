(ns fmnoise.dspec
  (:require [clojure.spec.alpha :as s]))

(defmacro defspec [method-name spec-name]
  `(do
     (defmulti ~method-name identity)
     (defmethod ~method-name :default [m#]
       (let [specs# (set (keys (methods ~method-name)))
             key# (some specs# (keys m#))]
         (when key# (~method-name key#))))
     (s/def ~spec-name (s/multi-spec ~method-name ~spec-name))))

(defmacro extend-spec [method-name spec-name keys]
  `(do ~@(map (fn [k] `(defmethod ~method-name ~k [~'_] ~spec-name)) keys)))
