(ns juxt-tech-test.core-test
  (:require [clojure.test :refer :all]
            [juxt-tech-test.core :refer :all]
            [clj-http.client :as c]
            [cheshire.core :refer [generate-string]])
  (:use clj-http.fake))

(deftest a-test
  (testing "Passes"
           (is (= 0 0))))

(def hottest-day-data
  [
   {"temperatureMax" 5 "time" 1509858000 "icon" "cloudy"}
   {"temperatureMax" 200 "time" 1509944400 "icon" "cloudy"}
   {"temperatureMax" 50 "time" 1510030800 "icon" "cloudy"}
   {"temperatureMax" 100 "time" 1510117200 "icon" "rainy"}
   {"temperatureMax" 0.5 "time" 1510203600 "icon" "clear"}
   ])

(deftest 
  find-hottest-day-test
  (testing "if find-hottest-day fn can find the hottest day"
    (is (= (find-hottest-day hottest-day-data) (get hottest-day-data 1)))))

(deftest
  hottest-day-test
  (testing "if hottest-day fn can return the correct day tag"
    (is (clojure.string/includes? (hottest-day {"data" hottest-day-data}) "Monday"))))

(def cities
  [{
    "country" "AD"
    "name" "Sant Julià de Lòria"
    "lat" "42.46372"
    "lng" "1.49129"
  }
  {
    "country" "AD"
    "name" "Pas de la Casa"
    "lat" "42.54277"
    "lng" "1.73361"
  }
  {
    "country" "AD"
    "name" "Ordino"
    "lat" "42.55623"
    "lng" "1.53319"
  }
  {
    "country" "AD"
    "name" "les Escaldes"
    "lat" "42.50729"
    "lng" "1.53414"
  }])

(with-fake-routes
  {
   "https://raw.githubusercontent.com/lutangar/cities.json/master/cities.json"
   (fn [request] {:status 200 :body (generate-string cities)})
   "https://api.darksky.net/forecast/1f9fcb72e6b5d043a34b34bc5f4f86e1/60.59329987,-1.44250533"
   (fn [request]
     (println "intercepted")
     {:status 200 :body (slurp "./darksky_payload.json")})
   })

(deftest
  get-forecast-test
  (testing "if get-forecast fn will return the correct string"
    (is (= (get-forecast 60.59329987,-1.44250533)
           "Current weather - Clear, Today we will see - Partly cloudy throughout the day. with a 0% chance of rain"))))

(deftest icon-frequency-test
  (testing "if icon-frequency fn will return with the correct string"
    (is (= (icon-frequency {"data" hottest-day-data})
           "This week we should have 3 cloudy days 1 rainy days 1 clear days"))))

;unable to finish testing for hottest month and get-forecast-with-city
;would need to colate some data for adequate testing