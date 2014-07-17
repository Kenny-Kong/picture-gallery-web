(defproject picture-gallery "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [postgresql/postgresql "9.1-901.jdbc4"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [lib-noir "0.7.6"]
                 [com.taoensso/timbre "2.6.1"]
                 [com.postspectacular/rotor "0.1.0"]
                 [environ "0.4.0"]
                 [selmer "0.5.4"]
                 [org.clojure/tools.reader "0.7.10"]
                 [org.clojure/clojurescript "0.0-1806"]
                 [domina "1.0.0"]
                 [cljs-ajax "0.2.0"]
                 [ring-middleware-format "0.3.1"]
                 [korma "0.3.0-RC5"]
                 [log4j "1.2.15"
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdmk/jmxtools
                               com.sun.jmx/jmxri]]
                 ]
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.4.0"]
            [lein-cljsbuild "0.3.2"]]
  :ring {:handler picture-gallery.handler/app
         :init picture-gallery.handler/init
         :destroy picture-gallery.handler/destroy}
  :aot :all
  :profiles
  {:production
   {:ring
    {:open-browser? false, 
     :stacktraces? false, 
     :auto-reload? false}
    :env {:port 3000
          :db-url "//localhost/gallery"
          :db-user "postgres"
          :db-pass "postgres"
          :galleries-path "galleries"}
    }
   :dev
   {:dependencies [[ring-mock "0.1.5"] 
                   [ring/ring-devel "1.2.1"]]
    :env {:port 3000
          :db-url "//localhost/gallery"
          :db-user "postgres"
          :db-pass "postgres"
          :galleries-path "galleries"}}}
  :cljsbuild
  {:builds
   ;   [{:source-paths ["src-cljs"]
   ;     :compiler
   ;     {:pretty-print false
   ;      :output-to "resources/public/js/gallery-cljs.js"}}]
   {:dev {:source-paths ["src-cljs"]
          :compiler
          {:pretty-print true
           :output-to "resources/public/js/gallery-cljs.js"}}
    :prod {:source-paths ["src-cljs"]
           :compiler
           {
            ;:optimizations :advanced
            :pretty-print true
            :externs ["resources/externs.js"]
            :output-to "resources/public/js/gallery-cljs.js"}}}
   }
  )
