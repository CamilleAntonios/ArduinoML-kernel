
cd kernels/jvm
mvn clean install

cd ../../embeddeds/groovy/GroovuinoML
mvn install
mvn clean compile assembly:single
