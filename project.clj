(defproject juxt-tech-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.10.1"]
                 [cheshire "5.10.0"]
                 [clj-time "0.15.2"]]
  :main ^:skip-aot juxt-tech-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[clj-http-fake "1.0.3"]]}})
