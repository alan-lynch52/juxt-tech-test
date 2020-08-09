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


(defn day-abbreviation->full-day
  [day]
  (get
   {"mon" "Monday"
    "tue" "Tuesday"
    "wed" "Wednesday"
    "thu" "Thursday"
    "fri" "Friday"
    "sat" "Saturday"
    "sun" "Sunday"}
   (lower-case day)))

(defn find-hottest-day
  [data]
  (-> (sort-by #(get % "temperatureMax") data)
      last ;last item will be the item with the largest temperatureMax
      ))

(defn hottest-day
  [{:strs [data]}]
  (-> (find-hottest-day data)
      (get "time")
      long
      (* 1000)
      c/from-long
      c/to-date ;convert to java time, as this contains the day string
      str
      (split #" ")
      first
      day-abbreviation->full-day
      (->> (str "This week the hottest day will be "))))

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
  (println (get-forecast-with-city "pas de la casa"))
  (println "END PART 2")
  (let [{:strs [daily]} (get-darksky-forecast 60.59329987 -1.44250533)]
    (println "PART 3")
    (println (hottest-day daily))
    (println "END PART 3")))
