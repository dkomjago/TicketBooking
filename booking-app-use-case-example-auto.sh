#!/bin/bash
appHost=${1:-localhost}
appPort=${2:-8080}

printf "\n///\nSELECT TIME -> GET SCREENINGS\n///\n"
curl -sS -X GET "http://$appHost:$appPort/api/booking/screenings?from=2016-11-16%2006%3A43&to=2030-11-16%2006%3A43" -H  "accept: */*" | jq .
printf "\n///\nSELECT SCREENING -> GET AVAILABLE SEATS\n///\n"
screeningId=$(curl -sS -X GET "http://$appHost:$appPort/api/booking/screenings?from=2016-11-16%2006%3A43&to=2030-11-16%2006%3A43" -H  "accept: */*" | jq ".screenings[0].id")
curl -sS -X GET "http://$appHost:$appPort/api/booking/screening/$screeningId" -H  "accept: */*" | jq .
printf "\n///\nBOOK SEAT -> GET PRICE AND EXPIRATION TIME\n///\n"
curl -sS -X POST "http://$appHost:$appPort/api/booking/book" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"selectedSeats\": [    {      \"ticketTypeId\": 0,      \"seatId\": 1    }  ],  \"screeningId\": $screeningId,  \"name\": \"String\"}" | jq .