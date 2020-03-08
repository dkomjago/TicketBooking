///BUILD and RUN
1. run 'booking app build and run'.sh (with no arguments for running on port '8080' otherwise write port as argument)
!!!WARNING!!! requires java 8+ and internet connection

///RUN DEMO AUTOMATIC
1. install jq
2. run 'booking app use case example auto'.sh (with no arguments for 'localhost:8080' otherwise arguments are {1}host {2}port)
!!!WARNING!!! books only first seat of first screening, so repeated use will result in "Seat already booked response"

///RUN DEMO MANUAL
1. run 'booking app use case example manual'.sh (with no arguments for 'localhost:8080' otherwise arguments are {1}host {2}port)
2. enter values as prompted by console

///API INFO
endpoints:
1.getting list of all screenings in a time frame -> /api/booking/screenings GET requires query parameters, example: from=2016-11-16 06:43 to=2020-11-16 06:43
2.getting screening information -> /api/booking/screening/{screeningId} GET requires valid screening id as path argument
3.booking seats -> /api/booking/book POST  requires json, example:{\"selectedSeats\": [{\"ticketTypeId\": 0,\"seatId\": 1}],\"screeningId\": $screeningId,\"name\":\"String\",\"surname\":\"String\"}

for easier api testing visit swagger ui endpoint /swagger-ui.html

///ADDITIONAL ASSUMPTIONS MADE
1.reservation expiration time is set to 1 hour
2.polish characters are unicode compliant
3.money values are of Joda Money type to support different currencies
4.all time values are local
5.database is in-memory H2
