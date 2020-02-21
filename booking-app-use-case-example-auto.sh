#!/bin/bash
appHost=${1:-localhost}
appPort=${2:-8080}

printf "\n///\nSELECT TIME -> GET SCREENINGS\n///\n"
curl -sS -X POST "http://$appHost:$appPort/api/booking/screenings" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"from\": \"2016-11-16 06:43\",  \"to\": \"2020-11-16 06:43\"}" | jq .
printf "\n///\nSELECT SCREENING -> GET AVAILABLE SEATS\n///\n"
screeningId=$(curl -sS -X POST "http://$appHost:$appPort/api/booking/screenings" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"from\": \"2016-11-16 06:43\",  \"to\": \"2020-11-16 06:43\"}" | jq ".screenings[0].id")
curl -sS -X GET "http://$appHost:$appPort/api/booking/screening/$screeningId" -H  "accept: */*" | jq .
printf "\n///\nBOOK SEAT -> GET PRICE AND EXPIRATION TIME\n///\n"
curl -sS -X POST "http://$appHost:$appPort/api/booking/book" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"selectedSeats\": [    {      \"ticketTypeId\": 0,      \"seatId\": 1    }  ],  \"screeningId\": $screeningId,  \"name\": \"String\",  \"surname\": \"String\"}" | jq .