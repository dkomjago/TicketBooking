applicationPort=${1:-8080}
./mvnw clean install && java -Dserver.port=$applicationPort -jar "target/TicketBooking-0.0.1-SNAPSHOT.jar" &