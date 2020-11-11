# PRODO-External-API-Examples


Um die externe API zum Abruf der Analysedaten zu benutzen, sind folgende Schritte notwendig

# Abruf des Authentifizierungstoken (JWT)
Es wird folgende Schnittstelle abgerufen werden müssen, um ein entsprechendes JWT zu erhalten.
Schnittstelle:

GET https://prodo-api.ariva-services.de/api/v1/authentication

Parameter:

* username
* password

# Nutzung des JWT
Mit diesem JWT kann dann die entsprechende Schnittstelle abgerufen werden
## Schnittstellen:

### Schnittstelle 1: 
POST https://prodo-api.ariva-services.de/api/v1/multiAggregation/sum

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
 
POST https://prodo-api.ariva-services.de/api/v1/multiAggregation/sum/csv

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
POST https://prodo-api.ariva-services.de/api/v1/multiAggregation/timeFrame

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
POST https://prodo-api.ariva-services.de/api/v1/multiAggregation/timeFrame/csv

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

Das JWT muss hierbei jeweils im Header übergeben werden. Entsprechende Beispiel für Java Spring Boot und Postman sind weiter unten zu finden

# Implementierung mit Postman
Link zu Postman: https://www.postman.com/downloads/
Damit kann man z.B. folgendes Curl-Kommando als raw text importieren:

    curl -X POST "https://prodo-api.ariva-services.de/api/v1/multiAggregation/sum" -H  "accept: */*" -H  "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZR0tJd1kwMGQ0Y3ZYS0RJNDNrN1Q3NklDZXpBaENQTHBQREltSkdpeEhVIn0.eyJleHAiOjE2MDQ2OTQwMjAsImlhdCI6MTYwNDY2NTIyMCwianRpIjoiNTYyMTRiYWYtNDEwNi00ZmEyLWI5Y2EtNWRiNjgzNjgxNDRmIiwiaXNzIjoiaHR0cHM6Ly9wcm9kby1zc28tdGkuYXJpdmEtc2VydmljZXMuZGUvYXV0aC9yZWFsbXMvUFJPRE8iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiMDM5NjY1ODctNWIwNC00Y2Y5LThiMmUtMTAwODQyZjZlZmMxIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYmFja2VuZFJlYWQiLCJzZXNzaW9uX3N0YXRlIjoiODgwZTZjZWEtZTliZS00OWE4LWEzZTgtZmZhMjY0YzFmNjc3IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJBZHZlcnRpc2VyLXN3X3VybCIsIkFkdmVydGlzZXItVm9udG9iZWwiLCJBZHZlcnRpc2VyIENvbW1lcnpiYW5rIiwiUHVibGlzaGVyLVplcnRpZmlrYXRlQW5sZWdlciIsIkFkdmVydGlzZXItc3dfdGVzdF8xNV9tYWkiLCJQdWJsaXNoZXIgQVJJVkEiLCJQdWJsaXNoZXItc3d1cmwiLCJBZ2VudHVyIChDb21tZXJ6YmFuayAvIFZvbnRvYmVsKSIsIlB1Ymxpc2hlciBXTyIsIkFkdmVydGlzZXIiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiVmVyd2FsdHVuZyIsIlB1Ymxpc2hlci1zd3VybDMiLCJQdWJsaXNoZXItT25WaXN0YSIsIlZlcm1hcmt0ZXIiLCJBZHZlcnRpc2VyLUhTQkMiLCJQdWJsaXNoZXIiLCJBZG1pbiIsIkFkdmVydGlzZXItdGVzdCIsIkFkdmVydGlzZXItc3dfdGVzdCIsIlZlcm1hcmt0ZXIgKEFkaXNmYWN0aW9uKSIsIkFnZW50dXIiLCJBZHZlcnRpc2VyLWpoYV90ZXN0IiwiQWR2ZXJ0aXNlci1NYl90ZXN0Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJiYWNrZW5kUmVhZCI6eyJyb2xlcyI6WyJtcmVfdGVzdCJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJKZXNzZSBIYWFmIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamhhIiwibG9jYWxlIjoiZGUiLCJnaXZlbl9uYW1lIjoiSmVzc2UiLCJmYW1pbHlfbmFtZSI6IkhhYWYiLCJlbWFpbCI6Implc3NlLmhhYWZAYXJpdmEuZGUifQ.Femf9puFyCP1eL-c5YZGl7cW7y38DQJUYTbApzCXTld_dA37FaZGDF_OLjTjr3y4PpZXFxxubFbM59Ppkbk2hg9KmrZzdGtpj9ryPEDkYk78mljzIQ5_t1IXAXVzTcjH8-YXFgAjFlFZpaWaH_dsxDAJ06FmWcopQgHcexfCRFx4mEbPpM38zw7lPcLzysn_9EgTilVvGpGew6hWJ29BwFUkbBBKM2XGjwRIZ5YREX_byYH8SAEvI-3abAbzPLj9d5Tqhg-1MH5T1KIhOKIQLB-kIdwKvvc3lIbuxPNUXL3iE06qqF5UJXkRsm89EaSEqaWRjLAp1iDXMVG9Ni_9aw" -H  "Content-Type: application/json" -d "{  \"adSlotIds\": [],  \"advertiserIds\": [],  \"aggregationDimension\": \"PUBLISHER\",  \"bouquetIds\": [],  \"devices\": [],  \"endDate\": \"2020-11-06\",  \"language\": \"de\",  \"publisherIds\": [],  \"regions\": [],  \"startDate\": \"2020-10-01\"}"

Hier noch Beispiel-curl-Kommandos für die anderen Schnittstellen:

    curl -X POST "https://prodo-api.ariva-services.de/api/v1/multiAggregation/sum/csv" -H  "accept: application/octet-stream" -H  "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZR0tJd1kwMGQ0Y3ZYS0RJNDNrN1Q3NklDZXpBaENQTHBQREltSkdpeEhVIn0.eyJleHAiOjE2MDQ2OTQwMjAsImlhdCI6MTYwNDY2NTIyMCwianRpIjoiNTYyMTRiYWYtNDEwNi00ZmEyLWI5Y2EtNWRiNjgzNjgxNDRmIiwiaXNzIjoiaHR0cHM6Ly9wcm9kby1zc28tdGkuYXJpdmEtc2VydmljZXMuZGUvYXV0aC9yZWFsbXMvUFJPRE8iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiMDM5NjY1ODctNWIwNC00Y2Y5LThiMmUtMTAwODQyZjZlZmMxIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYmFja2VuZFJlYWQiLCJzZXNzaW9uX3N0YXRlIjoiODgwZTZjZWEtZTliZS00OWE4LWEzZTgtZmZhMjY0YzFmNjc3IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJBZHZlcnRpc2VyLXN3X3VybCIsIkFkdmVydGlzZXItVm9udG9iZWwiLCJBZHZlcnRpc2VyIENvbW1lcnpiYW5rIiwiUHVibGlzaGVyLVplcnRpZmlrYXRlQW5sZWdlciIsIkFkdmVydGlzZXItc3dfdGVzdF8xNV9tYWkiLCJQdWJsaXNoZXIgQVJJVkEiLCJQdWJsaXNoZXItc3d1cmwiLCJBZ2VudHVyIChDb21tZXJ6YmFuayAvIFZvbnRvYmVsKSIsIlB1Ymxpc2hlciBXTyIsIkFkdmVydGlzZXIiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiVmVyd2FsdHVuZyIsIlB1Ymxpc2hlci1zd3VybDMiLCJQdWJsaXNoZXItT25WaXN0YSIsIlZlcm1hcmt0ZXIiLCJBZHZlcnRpc2VyLUhTQkMiLCJQdWJsaXNoZXIiLCJBZG1pbiIsIkFkdmVydGlzZXItdGVzdCIsIkFkdmVydGlzZXItc3dfdGVzdCIsIlZlcm1hcmt0ZXIgKEFkaXNmYWN0aW9uKSIsIkFnZW50dXIiLCJBZHZlcnRpc2VyLWpoYV90ZXN0IiwiQWR2ZXJ0aXNlci1NYl90ZXN0Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJiYWNrZW5kUmVhZCI6eyJyb2xlcyI6WyJtcmVfdGVzdCJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJKZXNzZSBIYWFmIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamhhIiwibG9jYWxlIjoiZGUiLCJnaXZlbl9uYW1lIjoiSmVzc2UiLCJmYW1pbHlfbmFtZSI6IkhhYWYiLCJlbWFpbCI6Implc3NlLmhhYWZAYXJpdmEuZGUifQ.Femf9puFyCP1eL-c5YZGl7cW7y38DQJUYTbApzCXTld_dA37FaZGDF_OLjTjr3y4PpZXFxxubFbM59Ppkbk2hg9KmrZzdGtpj9ryPEDkYk78mljzIQ5_t1IXAXVzTcjH8-YXFgAjFlFZpaWaH_dsxDAJ06FmWcopQgHcexfCRFx4mEbPpM38zw7lPcLzysn_9EgTilVvGpGew6hWJ29BwFUkbBBKM2XGjwRIZ5YREX_byYH8SAEvI-3abAbzPLj9d5Tqhg-1MH5T1KIhOKIQLB-kIdwKvvc3lIbuxPNUXL3iE06qqF5UJXkRsm89EaSEqaWRjLAp1iDXMVG9Ni_9aw" -H  "Content-Type: application/json" -d "{  \"adSlotIds\": [],  \"advertiserIds\": [],  \"aggregationDimension\": \"PUBLISHER\",  \"bouquetIds\": [],  \"devices\": [],  \"endDate\": \"2020-11-06\",  \"language\": \"de\",  \"publisherIds\": [],  \"regions\": [],  \"startDate\": \"2020-10-01\"}"

    curl -X POST "https://prodo-api.ariva-services.de/api/v1/multiAggregation/timeFrame" -H  "accept: */*" -H  "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZR0tJd1kwMGQ0Y3ZYS0RJNDNrN1Q3NklDZXpBaENQTHBQREltSkdpeEhVIn0.eyJleHAiOjE2MDQ2OTQwMjAsImlhdCI6MTYwNDY2NTIyMCwianRpIjoiNTYyMTRiYWYtNDEwNi00ZmEyLWI5Y2EtNWRiNjgzNjgxNDRmIiwiaXNzIjoiaHR0cHM6Ly9wcm9kby1zc28tdGkuYXJpdmEtc2VydmljZXMuZGUvYXV0aC9yZWFsbXMvUFJPRE8iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiMDM5NjY1ODctNWIwNC00Y2Y5LThiMmUtMTAwODQyZjZlZmMxIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYmFja2VuZFJlYWQiLCJzZXNzaW9uX3N0YXRlIjoiODgwZTZjZWEtZTliZS00OWE4LWEzZTgtZmZhMjY0YzFmNjc3IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJBZHZlcnRpc2VyLXN3X3VybCIsIkFkdmVydGlzZXItVm9udG9iZWwiLCJBZHZlcnRpc2VyIENvbW1lcnpiYW5rIiwiUHVibGlzaGVyLVplcnRpZmlrYXRlQW5sZWdlciIsIkFkdmVydGlzZXItc3dfdGVzdF8xNV9tYWkiLCJQdWJsaXNoZXIgQVJJVkEiLCJQdWJsaXNoZXItc3d1cmwiLCJBZ2VudHVyIChDb21tZXJ6YmFuayAvIFZvbnRvYmVsKSIsIlB1Ymxpc2hlciBXTyIsIkFkdmVydGlzZXIiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiVmVyd2FsdHVuZyIsIlB1Ymxpc2hlci1zd3VybDMiLCJQdWJsaXNoZXItT25WaXN0YSIsIlZlcm1hcmt0ZXIiLCJBZHZlcnRpc2VyLUhTQkMiLCJQdWJsaXNoZXIiLCJBZG1pbiIsIkFkdmVydGlzZXItdGVzdCIsIkFkdmVydGlzZXItc3dfdGVzdCIsIlZlcm1hcmt0ZXIgKEFkaXNmYWN0aW9uKSIsIkFnZW50dXIiLCJBZHZlcnRpc2VyLWpoYV90ZXN0IiwiQWR2ZXJ0aXNlci1NYl90ZXN0Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJiYWNrZW5kUmVhZCI6eyJyb2xlcyI6WyJtcmVfdGVzdCJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJKZXNzZSBIYWFmIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamhhIiwibG9jYWxlIjoiZGUiLCJnaXZlbl9uYW1lIjoiSmVzc2UiLCJmYW1pbHlfbmFtZSI6IkhhYWYiLCJlbWFpbCI6Implc3NlLmhhYWZAYXJpdmEuZGUifQ.Femf9puFyCP1eL-c5YZGl7cW7y38DQJUYTbApzCXTld_dA37FaZGDF_OLjTjr3y4PpZXFxxubFbM59Ppkbk2hg9KmrZzdGtpj9ryPEDkYk78mljzIQ5_t1IXAXVzTcjH8-YXFgAjFlFZpaWaH_dsxDAJ06FmWcopQgHcexfCRFx4mEbPpM38zw7lPcLzysn_9EgTilVvGpGew6hWJ29BwFUkbBBKM2XGjwRIZ5YREX_byYH8SAEvI-3abAbzPLj9d5Tqhg-1MH5T1KIhOKIQLB-kIdwKvvc3lIbuxPNUXL3iE06qqF5UJXkRsm89EaSEqaWRjLAp1iDXMVG9Ni_9aw" -H  "Content-Type: application/json" -d "{  \"adSlotIds\": [],  \"advertiserIds\": [],  \"aggregationCountType\": \"CLICKS\",  \"aggregationDimension\": \"PUBLISHER\",  \"aggregationType\": \"COMPLETE\",  \"bouquetIds\": [],  \"devices\": [],  \"endDate\": \"2020-11-06\",  \"language\": \"de\",  \"publisherIds\": [],  \"regions\": [],  \"startDate\": \"2020-10-01\"}"

    curl -X POST "https://prodo-api.ariva-services.de/api/v1/multiAggregation/timeFrame/csv" -H  "accept: application/octet-stream" -H  "Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZR0tJd1kwMGQ0Y3ZYS0RJNDNrN1Q3NklDZXpBaENQTHBQREltSkdpeEhVIn0.eyJleHAiOjE2MDQ2OTQwMjAsImlhdCI6MTYwNDY2NTIyMCwianRpIjoiNTYyMTRiYWYtNDEwNi00ZmEyLWI5Y2EtNWRiNjgzNjgxNDRmIiwiaXNzIjoiaHR0cHM6Ly9wcm9kby1zc28tdGkuYXJpdmEtc2VydmljZXMuZGUvYXV0aC9yZWFsbXMvUFJPRE8iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiMDM5NjY1ODctNWIwNC00Y2Y5LThiMmUtMTAwODQyZjZlZmMxIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYmFja2VuZFJlYWQiLCJzZXNzaW9uX3N0YXRlIjoiODgwZTZjZWEtZTliZS00OWE4LWEzZTgtZmZhMjY0YzFmNjc3IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJBZHZlcnRpc2VyLXN3X3VybCIsIkFkdmVydGlzZXItVm9udG9iZWwiLCJBZHZlcnRpc2VyIENvbW1lcnpiYW5rIiwiUHVibGlzaGVyLVplcnRpZmlrYXRlQW5sZWdlciIsIkFkdmVydGlzZXItc3dfdGVzdF8xNV9tYWkiLCJQdWJsaXNoZXIgQVJJVkEiLCJQdWJsaXNoZXItc3d1cmwiLCJBZ2VudHVyIChDb21tZXJ6YmFuayAvIFZvbnRvYmVsKSIsIlB1Ymxpc2hlciBXTyIsIkFkdmVydGlzZXIiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiVmVyd2FsdHVuZyIsIlB1Ymxpc2hlci1zd3VybDMiLCJQdWJsaXNoZXItT25WaXN0YSIsIlZlcm1hcmt0ZXIiLCJBZHZlcnRpc2VyLUhTQkMiLCJQdWJsaXNoZXIiLCJBZG1pbiIsIkFkdmVydGlzZXItdGVzdCIsIkFkdmVydGlzZXItc3dfdGVzdCIsIlZlcm1hcmt0ZXIgKEFkaXNmYWN0aW9uKSIsIkFnZW50dXIiLCJBZHZlcnRpc2VyLWpoYV90ZXN0IiwiQWR2ZXJ0aXNlci1NYl90ZXN0Il19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJiYWNrZW5kUmVhZCI6eyJyb2xlcyI6WyJtcmVfdGVzdCJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsIm5hbWUiOiJKZXNzZSBIYWFmIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiamhhIiwibG9jYWxlIjoiZGUiLCJnaXZlbl9uYW1lIjoiSmVzc2UiLCJmYW1pbHlfbmFtZSI6IkhhYWYiLCJlbWFpbCI6Implc3NlLmhhYWZAYXJpdmEuZGUifQ.Femf9puFyCP1eL-c5YZGl7cW7y38DQJUYTbApzCXTld_dA37FaZGDF_OLjTjr3y4PpZXFxxubFbM59Ppkbk2hg9KmrZzdGtpj9ryPEDkYk78mljzIQ5_t1IXAXVzTcjH8-YXFgAjFlFZpaWaH_dsxDAJ06FmWcopQgHcexfCRFx4mEbPpM38zw7lPcLzysn_9EgTilVvGpGew6hWJ29BwFUkbBBKM2XGjwRIZ5YREX_byYH8SAEvI-3abAbzPLj9d5Tqhg-1MH5T1KIhOKIQLB-kIdwKvvc3lIbuxPNUXL3iE06qqF5UJXkRsm89EaSEqaWRjLAp1iDXMVG9Ni_9aw" -H  "Content-Type: application/json" -d "{  \"adSlotIds\": [],  \"advertiserIds\": [],  \"aggregationCountType\": \"CLICKS\",  \"aggregationDimension\": \"PUBLISHER\",  \"aggregationType\": \"COMPLETE\",  \"bouquetIds\": [],  \"devices\": [],  \"endDate\": \"2020-11-06\",  \"language\": \"de\",  \"publisherIds\": [],  \"regions\": [],  \"startDate\": \"2020-10-01\"}"

Natürlich muss man sich nach dem Import noch den JWT-Token holen und dann in Postman im Authorization-Header austauschen. Um sich den Token zu holen, kann man folgendes curl-Kommando importieren und die Parameter entsprechend setzen:

    curl -X GET "https://prodo-api.ariva-services.de/api/v1/authentication?password=password&username=username" -H  "accept: */*"

# Implementierung mit Java Spring Boot
## Api-Abrufe als wichtige Codesnippets:
Das restTemplate ist ein org.springframework.web.client.RestTemplate und die apiServiceURL ist "https://prodo-api.ariva-services.de/api/v1":

       public String getAuthentication(String username, String password) {
          ResponseEntity<String> response = restTemplate.getForEntity(
                  apiServiceURL + "/authentication?username={username}&password={password}", String.class, username, password);
    
          return response.getBody();
       }

Hier werden noch Header gesetzt, u.a. der Content-Type und der Authorization-Header; das ist ein POST-Request mit Request-Body (s.o.):

       public MultiSumAggregationDTO getMultiSumAggregation(MultiSumAggregationRequestBodyDTO requestBody, String jwtToken) {
          HttpHeaders headers = new HttpHeaders();
          headers.setAccept(Collections.singletonList(MediaType.ALL));
          headers.setContentType(MediaType.APPLICATION_JSON);
          headers.set("Authorization", "bearer " + jwtToken);
    
          HttpEntity<MultiSumAggregationRequestBodyDTO> entity = new HttpEntity<>(requestBody, headers);
          ResponseEntity<MultiSumAggregationDTO> response = restTemplate.exchange(
                  apiServiceURL + "/multiAggregation/sum", HttpMethod.POST, entity, MultiSumAggregationDTO.class);
    
          return response.getBody();
       }

Im Beispiel-Projekt läuft ein minütlicher Scheduled-Job, welcher die Multi-Sum-Aggregation der letzten 3 Monate abruft und das Ergebnis auf der Konsole ausgibt. Der Authentifizierungs-JWT-Token wird vorher abgerufen und der Multi-SumAggregation-Request mit einem entsprechenden Authorisierungs-Header versehen (siehe Codesnippet oben).