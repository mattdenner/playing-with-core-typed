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

; Here's a higher order function which is essentially a curried version of addTwoNumbers.  In Haskell, where all
; functions are curried by default, you'd drop the 'Fn' bit:
;         higherOrderFunctionForAddTwoNumbers :: Integer -> Integer -> Integer
(ann higherOrderFunctionForAddTwoNumbers [AnyInteger -> (Fn [AnyInteger -> AnyInteger])])
(defn higherOrderFunctionForAddTwoNumbers [a]
  (fn [b]
    (+ a b)))

; But you can imply the types too: this will error because I'm being more restrictive in the higher order function
; signature, than the inner function uses.  You'll see two errors: one is because the type checker could assume
; that the inner function is declared wrong, the other because it could assume that the higher order function is
; wrong.
(ann moreRestrictiveThanInnerFunction [AnyInteger -> (Fn [AnyInteger -> Integer])])
(defn moreRestrictiveThanInnerFunction [a]
  (ann-form #(+ a %)
            [AnyInteger -> AnyInteger]))

; Heterogeneous maps are probably most useful for dispatching, where on field in the map defines what can happen.
; So let's have a multimethod that will dispatch based on a particular field.
(ann takesParticularMapStructure [(HMap :mandatory {:a (U (Value "left") (Value "right"))}) -> Integer])
(defmulti takesParticularMapStructure (fn [values] (:a values)))
(defmethod takesParticularMapStructure "left"  [values] 1)
(defmethod takesParticularMapStructure "right" [values] 2)

; So the first two functions will be fine because they obey the type signature, but the other two are in trouble
; because they don't!  Notice too that these functions take no arguments and *are not* declared as [Nothing -> Integer]
(ann takeLeftPath [-> Integer])
(defn takeLeftPath  [] (takesParticularMapStructure {:a "left"}))

(ann takeRightPath [-> Integer])
(defn takeRightPath [] (takesParticularMapStructure {:a "right"}))

(ann dontTakeEitherPathBecauseOfWrongType [-> Integer])
(defn dontTakeEitherPathBecauseOfWrongType [] (takesParticularMapStructure {:a 1}))

(ann dontTakeEitherPathBecauseOfMissingValue [-> Integer])
(defn dontTakeEitherPathBecauseOfMissingValue [] (takesParticularMapStructure {:b "left"}))

(comment
  (check-ns))
