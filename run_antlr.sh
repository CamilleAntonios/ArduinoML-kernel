cd kernels/jvm
mvn clean install

cd ../../externals/antlr
mvn clean package
mvn exec:java -Dexec.args="src/main/resources/inputs/light_sensor_alarm.arduinoml"