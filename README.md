A simple GUI torrent app, has a tracker that keeps info about the torrents and
a client that does the job of a seed and a client.

Everything can be switched off and on at any time, the downloads will be paused,
just re-add them to continue.

./gradlew server -- cli server
./gradlew client -- cli client
./gradlew gui    -- gui client

cd build/libs
java -Djava.util.logging.config.file="../../src/logging.properties" -jar server.jar -- more logging


java -Djava.util.logging.config.file="../../src/logging.properties" -jar client.jar -port 8082 -stateDir . -tracker localhost


--
