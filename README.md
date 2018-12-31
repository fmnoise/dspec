# dspec

Dependent spec is a syntax sugar for defining multispecs which use unique attributes instead of type field

## Usage

```clojure
(require '[fmnoise.dspec :as ds])
(require '[clojure.spec.alpha :as s])

(s/def :image/src string?)
(s/def :image/alt string?)
(s/def :anchor/href string?)
(s/def :anchor/text string?)
(s/def ::image (s/keys :req [:image/src :image/alt]))
(s/def ::anchor (s/keys :req [:anchor/href :anchor/text]))

(ds/defspec tag ::tag)
;; image is tag with :image/src and :image/alt
(ds/extend-spec tag ::image [:image/src :image/alt])
;; achor is tag with :anchor/href and :anchor/text
(ds/extend-spec tag ::anchor [:anchor/href :anchor/text])

(s/valid? ::tag {:image/src "1.jpg" :image/alt "image"}) ;; => true
(s/valid? ::tag {:anchor/href "1.jpg" :anchor/text "image"}) ;; => true
(s/valid? ::tag {:image/src "1.jpg"}) ;; => false
(s/valid? ::tag {:anchor/href "1.jpg"})  ;; => false
(s/valid? ::tag {}) ;; => false

(s/explain-data ::tag (dissoc image-tag :image/alt))
;; => #:clojure.spec.alpha{:problems ({:path [#:image{:src "1.jpg"}],
;;                                     :pred (clojure.core/fn [%] (clojure.core/contains? % :image/alt)),
;;                                     :val #:image{:src "1.jpg"},
;;                                     :via [:user/tag :user/image], :in []}),
;;                         :spec :user/tag,
;;                         :value #:image{:src "1.jpg"}}

(s/explain-data ::tag {})
;; => #:clojure.spec.alpha{:problems [{:path [{}],
;;                                     :pred user/tag,
;;                                     :val {},
;;                                     :reason "no method",
;;                                     :via [:user/tag],
;;                                     :in []}],
;;                         :spec :user/tag,
;;                         :value {}}
```

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
