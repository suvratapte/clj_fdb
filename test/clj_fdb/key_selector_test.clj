(ns clj-fdb.key-selector-test
  (:require
   [clj-fdb.core :as fc]
   [clj-fdb.transaction :as ftr]
   [clj-fdb.FDB :as cfdb]
   [byte-streams :as bs]
   [clj-fdb.key-selector :refer :all]
   [clojure.test :refer :all]))


(deftest test-constructors-and-getters
  (testing "Test the getter functions"
    (let [key-selectors-and-expected-results
          [{:ks (last-less-than (.getBytes "A"))
            :key "A" :offset 0}
           {:ks (last-less-or-equal (.getBytes "B"))
            :key "B" :offset 0}
           {:ks (first-greater-than (.getBytes "C"))
            :key "C" :offset 1}
           {:ks (first-greater-or-equal (.getBytes "D"))
            :key "D" :offset 1}]]
      (doseq [ks key-selectors-and-expected-results]
        (is (= (bs/to-string (get-key (:ks ks))) (:key ks))
            (= (get-offset (:ks ks)) (:offset ks)))))))


(deftest test-add
  (let [ks-1 (last-less-than (.getBytes "A"))
        new-ks-1 (add ks-1 10)
        ks-2 (first-greater-than (.getBytes "A"))
        new-ks-2 (add ks-2 -5)]
    (is (= (get-offset new-ks-1) 10)
        (= (get-offset new-ks-2) -4))))
