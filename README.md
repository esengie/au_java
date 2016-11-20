./gradlew server
./gradlew client

cd build/libs
java -Djava.util.logging.config.file="../../src/logging.properties" -jar server.jar 8082

java -Djava.util.logging.config.file="../../src/logging.properties" -jar client.jar -port 8082 -server localhost


--
