(ns fmnoise.dspec-test
  (:require [clojure.test :refer :all]
            [fmnoise.dspec :refer :all]
            [clojure.spec.alpha :as s]))

(defspec tag ::tag)
(s/def :image/src string?)
(s/def :image/alt string?)
(s/def :anchor/href string?)
(s/def :anchor/text string?)
(s/def ::image (s/keys :req [:image/src :image/alt]))
(s/def ::anchor (s/keys :req [:anchor/href :anchor/text]))
(extend-spec tag ::image [:image/src :image/alt])
(extend-spec tag ::anchor [:anchor/href :anchor/text])

(deftest dspec--test
  (let [image-tag {:image/src "1.jpg" :image/alt "image"}
        anchor-tag {:anchor/href "1.jpg" :anchor/text "image"}]
    (is (s/valid? ::tag image-tag))
    (is (s/valid? ::tag anchor-tag))
    (is (not (s/valid? ::tag (dissoc image-tag :image/alt))))
    (is (not (s/valid? ::tag (dissoc anchor-tag :anchor/href))))
    (is (= {::s/problems '({:path [{:image/src "1.jpg"}]
                            :pred (clojure.core/fn [%]
                                    (clojure.core/contains? % :image/alt))
                            :val {:image/src "1.jpg"}
                            :via [::tag ::image]
                            :in []})
            ::s/spec ::tag
            ::s/value {:image/src "1.jpg"}}
           (s/explain-data ::tag (dissoc image-tag :image/alt) )))))
