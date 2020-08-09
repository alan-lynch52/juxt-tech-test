(ns juxt-tech-test.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [cheshire.core :refer [parse-string generate-string]]
            [clojure.string :refer [lower-case split trim]]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(comment 
 "wouldn't usually put secrets in github repo
 however for simplicity it will be left in")
(def api-key "1f9fcb72e6b5d043a34b34bc5f4f86e1") 

(def base-url "https://api.darksky.net/forecast")

(def cities-url "https://raw.githubusercontent.com/lutangar/cities.json/master/cities.json")

(defn get-darksky-forecast
  [latitude longitude & [time]]
  (let [url (cond-> (str base-url "/" api-key "/" latitude "," longitude)
              time (str "," time))]
    (-> (client/get url)
        :body
        parse-string)))

(defn get-forecast
  [latitude longitude]
  (let [{:strs [currently hourly]} (get-darksky-forecast latitude longitude)]
    (str "Current weather - "
         (get currently "summary")
         ", Today we will see - "
         (get hourly "summary")
         " with a "
         (get currently "precipProbability")
         "% chance of rain")))


(defn get-forecast-with-city
  [city]
  (let [same-city? #(= (lower-case city) (lower-case (get % "name")))
        {:strs [lat lng]
         :as res} (-> (client/get cities-url)
                      :body
                      parse-string
                      (->> (filter same-city?))
                      first)]
    (if (seq res)
      (get-forecast lat lng)
      (str "No results found for the city " city))))


(defn -main
  [& args]
  (println "PART 1")
  (println (get-forecast 60.59329987 -1.44250533))
  (println "END PART 1")
  (println "PART 2")
  (println (get-forecast-with-city "pas de la casa")))
