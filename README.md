# PRODO-External-API-Examples


Um die externe API zum Abruf der Analysedaten zu benutzen, sind folgende Schritte notwendig

## Schnittstellen:

### Schnittstelle 1: 
POST https://prodo-api.ariva-services.de/api/v1/multiAggregation/sum/programmatic?apiToken=YOURAPITOKENHERE

JSON-POST-Body (nach // folgen Kommentare zum Feld; das language-Feld kann auch weggelassen werden, da es hier keine Auswirkung hat):

    {
      "adSlotIds": [
        "uuid-string" // leere Liste bedeutet keine Einschränkung
      ],
      "advertiserIds": [
        "uuid-string" // leere Liste bedeutet Einschränkung auf erlaubte
      ],
      "aggregationDimension": "PUBLISHER", // kann auch die Werte "ADVERTISER", "AD_SLOT" oder "BOUQUET" haben
      "bouquetIds": [
        "uuid-string" // leere Liste bedeutet keine Einschränkung
      ],
      "devices": [
        "DESKTOP", "MOBILE", "TABLET" // leere Liste bedeutet keine Einschränkung
      ],
      "endDate": "date-format-string", // Datumsformat ist yyyy-MM-dd
      "language": "language-string", // "de" für Deutsch und "en" für Englisch
      "publisherIds": [
        "uuid-string" // leere Liste bedeutet Einschränkung auf erlaubte
      ],
      "regions": [
        "D", "A", "CH", "WORLD" // leere Liste bedeutet keine Einschränkung
      ],
      "startDate": "date-format-string" // Datumsformat ist yyyy-MM-dd
     
    }

Antwort-Format für beispielsweise die Aggregations-Dimension "PUBLISHER" (für jeden Publisher wird die Gesamtzahl der Klicks und Views, sowie die Klickrate berechnet):

    [
      {
        "id": "publisher-uuid-string-1",
        "name": "publisher-name-1",
        "clickCount": zahl-1, // Aufsummierte Anzahl Klicks für den Zeitraum eingeschränkt auf die anderen Ids, Devices und Regionen aus dem Request-Body, Datentyp ist integer bzw. Ganzzahl
        "viewCount": zahl-2, // Aufsummierte Anzahl Views, Datentyp ist integer bzw. Ganzzahl
        "ctr": zahl-3 // Klickrate in % (Formel: (clickCount / viewCount) * 100), Datentyp ist double bzw. eine Dezimalzahl
      },
      {
        "id": "publisher-uuid-2",
        "name": "publisher-name-2",
        "clickCount": zahl-4,
        "viewCount": zahl-5,
        "ctr": zahl-6
      },
      ...
    ]

### Schnittstelle 2:
 
POST https://prodo-api.ariva-services.de/api/v1/multiAggregation/sum/csv/programmatic?apiToken=YOURAPITOKENHERE

JSON-POST-Body exakt wie in Schnittstelle 1 (das language-Feld muss allerdings hier gesetzt werden und wirkt sich auch aus):
Antwort-CSV-Format für beispielsweise die Aggregations-Dimension "PUBLISHER" (für jeden Publisher wird wie in der Schnittstelle 1 die Gesamtzahl der Klicks und Views, sowie die Klickrate berechnet; vgl. Antwort-JSON-Format):

    "NAME";"ANZAHL KLICKS";"ANZAHL IMPRESSIONEN";"KLICKRATE (CTR)"
    "publisher-name-1";"zahl-1";"zahl-2";"zahl-3" // Alle Zahlen stehen auch in Anführungszeichen; Dezimaltrenner bei der Klickrate ist ein Komma
    "publisher-name-2";"zahl-4";"zahl-5";"zahl-6"
    ...
    
    "NAME";"CLICK COUNT";"VIEW COUNT";"CLICK THROUGH RATE (CTR)"
    "publisher-name-1";"zahl-1";"zahl-2";"zahl-3" // Alle Zahlen stehen auch in Anführungszeichen; Dezimaltrenner bei der Klickrate ist ein Punkt
    "publisher-name-2";"zahl-4";"zahl-5";"zahl-6"
    ...

### Schnittstelle 3: 
POST https://prodo-api.ariva-services.de/api/v1/multiAggregation/timeFrame/programmatic?apiToken=YOURAPITOKENHERE

JSON-POST-Body (nach // folgen Kommentare zum Feld; das language-Feld kann auch weggelassen werden, da es hier keine Auswirkung hat; im Vergleich zu den ersten beiden Schnittstellen kommen die Felder aggregationCountType und aggregationType hinzu):

    {
      "adSlotIds": [
        "uuid-string" // leere Liste bedeutet keine Einschränkung
      ],
      "advertiserIds": [
        "uuid-string" // leere Liste bedeutet Einschränkung auf erlaubte
      ],
      "aggregationCountType": "CLICKS", // kann auch den Wert "VIEWS" haben
      "aggregationDimension": "PUBLISHER", // kann auch die Werte "ADVERTISER", "AD_SLOT" oder "BOUQUET" haben
      "aggregationType": "COMPLETE", // kann auch die Werte "MONTHLY", "WEEKLY", "DAILY"
      "bouquetIds": [
        "uuid-string" // leere Liste bedeutet keine Einschränkung
      ],
      "devices": [
        "DESKTOP", "MOBILE", "TABLET" // leere Liste bedeutet keine Einschränkung
      ],
      "endDate": "date-format-string", // Datumsformat ist yyyy-MM-dd
      "language": "language-string", // "de" für Deutsch und "en" für Englisch
      "publisherIds": [
        "uuid-string" // leere Liste bedeutet Einschränkung auf erlaubte
      ],
      "regions": [
        "D", "A", "CH", "WORLD" // leere Liste bedeutet keine Einschränkung
      ],
      "startDate": "date-format-string" // Datumsformat ist yyyy-MM-dd
    }

Antwort-Format für beispielsweise die Aggregations-Dimension "PUBLISHER" (für jeden Publisher wird die Gesamtzahl der Klicks oder Views für jeden Tag, jede Woche, jeden Monat oder komplett berechnet):

    [
      {
        "date": "date-string-1", // Datum (Format: yyyy-MM-dd) des Tages, der Woche (Montag als Wochenanfang), des Monats (erster Tag des Monats) oder null-Wert für Komplett-Aggregationen (je nach aggregationType)
        "values": [
          {
            "id": "publisher-uuid-string-1",
            "name": "publisher-name-1",
            "count": zahl-1 // Aufsummierte Anzahl Klicks (oder alternativ Views bei aggregationCountType "VIEWS") für den Tag, die Woche, den Monat oder komplett (je nach aggregationType) eingeschränkt auf die anderen Ids, Devices und Regionen aus dem Request-Body, Datentyp ist integer bzw. Ganzzahl
          },
          {
            "id": "publisher-uuid-string-2",
            "name": "publisher-name-2",
            "count": zahl-2
          }
        ]
      },
      {
        "date": "date-string-2", // Datum des nächsten Tages, der nächsten Woche oder des nächsten Monats (je nach aggregationType)
        "values": [
          {
            "id": "publisher-uuid-string-1",
            "name": "publisher-name-1",
            "count": zahl-3
          },
          {
            "id": "publisher-uuid-string-2",
            "name": "publisher-name-2",
            "count": zahl-4
          }
        ]
      },
      ...
    ]

### Schnittstelle 4: 
POST https://prodo-api.ariva-services.de/api/v1/multiAggregation/timeFrame/csv/programmatic?apiToken=YOURAPITOKENHERE

JSON-POST-Body exakt wie in Schnittstelle 3 (das language-Feld muss allerdings hier gesetzt werden und wirkt sich auch aus):

Antwort-CSV-Format für beispielsweise die Aggregations-Dimension "PUBLISHER" (für jeden Publisher wird wie in der Schnittstelle 3 die Gesamtzahl der Klicks oder Views für jeden Tag, jede Woche, jeden Monat oder komplett berechnet; vgl. Antwort-JSON-Format):

    "DATUM";"NAME";"ANZAHL"
    "date-string-1";"publisher-name-1";"zahl-1" // Die Zahl steht auch in Anführungszeichen; das deutsche Datumsformat ist dd.MM.yyyy
    "date-string-1";"publisher-name-2";"zahl-2"
    "date-string-2";"publisher-name-1";"zahl-3"
    "date-string-2";"publisher-name-2";"zahl-4"
    ...
    
    "DATE";"NAME";"COUNT"
    "date-string-1";"publisher-name-1";"zahl-1" // Die Zahl steht auch in Anführungszeichen; das englische Datumsformat ist yyyy-MM-dd
    "date-string-1";"publisher-name-2";"zahl-2"
    "date-string-2";"publisher-name-1";"zahl-3"
    "date-string-2";"publisher-name-2";"zahl-4"
    ...


# Implementierung mit Postman
Link zu Postman: https://www.postman.com/downloads/
Damit kann man z.B. folgendes Curl-Kommando als raw text importieren:

    curl -X POST "https://prodo-api.ariva-services.de/api/v1/multiAggregation/sum/programmatic?apiToken=YOURAPITOKENHERE" -H  "accept: */*"  -H  "Content-Type: application/json" -d "{  \"adSlotIds\": [],  \"advertiserIds\": [],  \"aggregationDimension\": \"PUBLISHER\",  \"bouquetIds\": [],  \"devices\": [],  \"endDate\": \"2020-11-06\",  \"language\": \"de\",  \"publisherIds\": [],  \"regions\": [],  \"startDate\": \"2020-10-01\"}"

Hier noch Beispiel-curl-Kommandos für die anderen Schnittstellen:

    curl -X POST "https://prodo-api.ariva-services.de/api/v1/multiAggregation/sum/csv/programmatic?apiToken=YOURAPITOKENHERE" -H  "accept: application/octet-stream"  -H  "Content-Type: application/json" -d "{  \"adSlotIds\": [],  \"advertiserIds\": [],  \"aggregationDimension\": \"PUBLISHER\",  \"bouquetIds\": [],  \"devices\": [],  \"endDate\": \"2020-11-06\",  \"language\": \"de\",  \"publisherIds\": [],  \"regions\": [],  \"startDate\": \"2020-10-01\"}"

    curl -X POST "https://prodo-api.ariva-services.de/api/v1/multiAggregation/timeFrame/programmatic?apiToken=YOURAPITOKENHERE" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"adSlotIds\": [],  \"advertiserIds\": [],  \"aggregationCountType\": \"CLICKS\",  \"aggregationDimension\": \"PUBLISHER\",  \"aggregationType\": \"COMPLETE\",  \"bouquetIds\": [],  \"devices\": [],  \"endDate\": \"2020-11-06\",  \"language\": \"de\",  \"publisherIds\": [],  \"regions\": [],  \"startDate\": \"2020-10-01\"}"

    curl -X POST "https://prodo-api.ariva-services.de/api/v1/multiAggregation/timeFrame/csv/programmatic?apiToken=YOURAPITOKENHERE" -H  "accept: application/octet-stream" -H  "Content-Type: application/json" -d "{  \"adSlotIds\": [],  \"advertiserIds\": [],  \"aggregationCountType\": \"CLICKS\",  \"aggregationDimension\": \"PUBLISHER\",  \"aggregationType\": \"COMPLETE\",  \"bouquetIds\": [],  \"devices\": [],  \"endDate\": \"2020-11-06\",  \"language\": \"de\",  \"publisherIds\": [],  \"regions\": [],  \"startDate\": \"2020-10-01\"}"


# Implementierung mit Java Spring Boot
## Api-Abrufe als wichtige Codesnippets:

Hier werden noch Header gesetzt, u.a. der Content-Type und der Authorization-Header; das ist ein POST-Request mit Request-Body (s.o.):

       public MultiSumAggregationDTO getMultiSumAggregation(MultiSumAggregationRequestBodyDTO requestBody, String jwtToken) {
          HttpHeaders headers = new HttpHeaders();
          headers.setAccept(Collections.singletonList(MediaType.ALL));
          headers.setContentType(MediaType.APPLICATION_JSON);
    
          HttpEntity<MultiSumAggregationRequestBodyDTO> entity = new HttpEntity<>(requestBody, headers);
          ResponseEntity<MultiSumAggregationDTO> response = restTemplate.exchange(
                  apiServiceURL + "/multiAggregation/sum/programmatic?apiToken=YOURAPITOKENHERE", HttpMethod.POST, entity, MultiSumAggregationDTO.class);
    
          return response.getBody();
       }

Im Beispiel-Projekt läuft ein minütlicher Scheduled-Job, welcher die Multi-Sum-Aggregation der letzten 3 Monate abruft und das Ergebnis auf der Konsole ausgibt. Der Authentifizierungs-JWT-Token wird vorher abgerufen und der Multi-SumAggregation-Request mit einem entsprechenden Authorisierungs-Header versehen (siehe Codesnippet oben).
