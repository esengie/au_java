A simple GUI torrent app, has a tracker that keeps info about the torrents and
a client that does the job of a seed and a client.

Everything can be switched off and on at any time, the downloads will be paused,
just re-add them to continue.

./gradlew server -- cli server

./gradlew client -- cli client

./gradlew gui    -- gui client

./gradlew localGui -- localGui (just a copy of gui to root)

java -jar gui.jar -port 8083 -stateDir . -tracker localhost

cd build/libs

java -jar server.jar -- more logging (-Djava.util.logging.config.file="../../src/logging.properties")

java -jar CLIclient.jar -port 8082 -stateDir . -tracker localhost

--
