(defproject in.co.sdslabs/slack-lens "0.0.1"
  :description
    "FIXME: write description"
  :url "FIXME: Add a url"
  :min-lein-version "2.0.0"
  :main "in.co.sdslabs.slack-lens.listener.run"
  :dependencies
    [[org.clojure/clojure "1.7.0"]
     [org.clojure/tools.cli "0.3.1"]
     [org.clojure/tools.logging "0.3.1"]
     [http-kit "2.2.0"]
     [org.clojure/core.async "0.1.346.0-17112a-alpha"]
     [ring/ring-core "1.3.2"]
     [ring/ring-jetty-adapter "1.3.2"]
     [metosin/ring-swagger-ui "2.1.5-M2"]
     [metosin/compojure-api "0.22.0"]
     [org.slf4j/slf4j-api "1.7.12"]
     [org.slf4j/slf4j-log4j12 "1.7.12"]
     [buddy/buddy-auth "2.1.0"]
     [log4j/apache-log4j-extras "1.2.17"]
     [ring.middleware.logger "0.5.0"]
     [de.ubercode.clostache/clostache "1.3.1"]
     [org.julienxx/clj-slack "0.4.3"]
     [aleph "0.4.0"]
     [manifold "0.1.0"]
     [cheshire "5.5.0"]
     [clojurewerkz/elastisch "2.1.0"]
     [clj-time "0.14.0"]]

  :profiles {:doc {:plugins [[lein-marginalia "0.8.0"]]}
             ;; uncomment the following line to prevent service logs from cluttering test output
             ; :test {:jvm-opts ["-Droot.logger=OFF"]}
  }

  :plugins
    [[lein-ring "0.9.6"]
     [lein-scss "0.3.0"]
     [lein-ancient "0.5.5"]
     [lein-cloverage "1.0.6"]]

  :ring
    {:port 40000
     :init in.co.sdslabs.slack-lens.service/initialize!
     :handler in.co.sdslabs.slack-lens.service/main-handler
     :destroy in.co.sdslabs.slack-lens.service/destroy!
     :host "localhost"}

  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (constantly true)}
  :jvm-opts
    ["-Xmx1g"
     "-server"
     "-Droot.logger=INFO, stdout"
     "-Dlog4j.configuration=file:resources/log4j.properties"]

  :aliases
    {"package" ["do" "clean," "javac," ["ring" "uberwar"]]
     "docs" ["marg" "-d" "target"]}

  :scss {:builds
         {:develop    {:source-dir "resources/scss/"
                       :dest-dir   "resources/public/css/"
                       :executable "sassc"
                       :args       ["-m" "-I" "resources/scss/" "-t" "nested"]}
          :production {:source-dir "resources/scss/"
                       :dest-dir   "resources/public/css/"
                       :executable "sassc"
                       :args       ["-I" "resources/scss/" "-t" "compressed"]}}}

  :publish {:local-prefix "target/coverage"
            :entry-file "index.html"
            :artifact-name "Coverage report"
            :remote-prefix "test-results/slack-lens/coverage"})
