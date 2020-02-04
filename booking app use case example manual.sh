appHost=${1:-localhost}
appPort=${2:-8080}

printf "\n///\nSTART\n///\n"
curl -sS -X POST "http://$appHost:$appPort/api/booking/screenings" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"from\": \"2016-11-16 06:43\",  \"to\": \"2020-11-16 06:43\"}"
printf "\n!!!Please enter screening id!!!\n"
read screeningId
curl -sS -X GET "http://$appHost:$appPort/api/booking/screening/$screeningId" -H  "accept: */*"
printf "\n!!!Please enter seat id!!!\n"
read seatId
printf "\n!!!Please enter ticket type id!!!\n"
read ticketTypeId
printf "\n!!!Please enter name!!!\n"
read customerName
printf "\n!!!Please enter surname!!!\n"
read customerSurname
curl -sS -X POST "http://$appHost:$appPort/api/booking/book" -H  "accept: */*" -H  "Content-Type: application/json" -d "{  \"selectedSeats\": [    {      \"ticketTypeId\": $ticketTypeId,      \"seatId\": $seatId    }  ],  \"screeningId\": $screeningId,  \"name\": \"$customerName\",  \"surname\": \"$customerSurname\"}"
printf "\n///\nEND\n///\n"