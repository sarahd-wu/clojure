(ns my-mini-clojure-project.core
  (:require [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.data.csv :as csv] 
            [clojure.java.io :as io]))

(def access-token "3NKHt6i2urmWtqOuugvr9faDiVlBV_OxaWSwYOjj3NRM5KZ_eKBEDYL4JWGpuXjrwBiq83-gPvf5w3n1zQe6J7ydk8_3bnXoOYkzJvulU-yLgGwViyqDGKR3HGOyQq-w")

(defn enrich-location []
  (let [study-areas [{:geometry {:x 139.78444 :y 35.68109}}]
        data-collections ["EducationEsriJapan"]
        params {:studyAreas (json/generate-string study-areas)
                :dataCollections (json/generate-string data-collections)
                :f "pjson"
                :token access-token}
        response (client/post "https://geoenrich.arcgis.com/arcgis/rest/services/World/geoenrichmentserver/Geoenrichment/enrich"
                              {:form-params params
                               :headers {"Content-Type" "application/x-www-form-urlencoded"}})]
    (println "Response:" response) ;; Print the full response
      (println "Body:" (:body response)) ;; Print just the body
      (:body response))) ;; Return the body
    
(enrich-location)

(defn write-to-csv [data]
  (with-open [writer (io/writer "results.csv")]
    (csv/write-csv writer
  ;; Writing the header row by extracting keys from the first map 
    (cons (keys (first data))
          (map vals data)))))

(defn fetch-and-save-csv []
  (let [data (enrich-location)]  ;; Fetch data from ArcGIS
    (write-to-csv data)))          ;; Save to CSV

(fetch-and-save-csv)