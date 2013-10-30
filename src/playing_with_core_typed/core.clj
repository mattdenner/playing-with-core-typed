(ns playing-with-core-typed.core
  (:use [clojure.core.typed :refer :all]))

; Simple function that adds two numbers which has a very very simple type signature.
(ann addTwoNumbers [AnyInteger AnyInteger -> AnyInteger])
(defn addTwoNumbers [a b]
  (+ a b))

; This function will not pass the type checking because it's too promiscuous to use addTwoNumbers.  Note that no
; indication, other than line number, says that this function is in error because it can't be implied; the type
; checker can only tell you that if 'a' & 'b' are Any then you can't call AddTwoNumbers.
(ann wayTooPromisuousForAddTwoNumbers [Any Any -> Any])
(defn wayTooPromisuousForAddTwoNumbers [a b]
  (addTwoNumbers a b))

; This function will also not pass the type checking because it is too restrctive on the return type.  Oddly it
; generates two errors here: one is because the return value AnyInteger is wider than Integer, the other is because
; the signature of the addTwoNumbers has that return type.  It's the latter that is actually more interesting.
(ann tooRestrictiveForReturnFromAddTwoNumbers [Integer Integer -> Integer])
(defn tooRestrictiveForReturnFromAddTwoNumbers [a b]
  (addTwoNumbers a b))

; This function, however, is fine:
(ann fineForAddTwoNumbers [Integer Integer -> Any])
(defn fineForAddTwoNumbers [a b]
  (addTwoNumbers a b))

(comment
  (check-ns))
