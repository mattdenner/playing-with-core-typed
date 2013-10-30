(defproject playing-with-core-typed "0.1.0-SNAPSHOT"
  :description "Messing about with clojure core.typed"
  :url "http://github.com/mattdenner/playing-with-core-typed"
  :license {:name "Eclipse Public License" :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"] [org.clojure/core.typed "0.2.14"]]
  :plugins [[lein-typed "0.3.1"]]
  :core.typed {:check [playing-with-core-typed.core]})
