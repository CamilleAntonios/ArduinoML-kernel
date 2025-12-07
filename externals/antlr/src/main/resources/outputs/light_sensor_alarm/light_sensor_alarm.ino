// Wiring code generated from an ArduinoML model
// Application name: lightSensorAlarm

long debounce = 200;

enum STATE {off, on};
STATE currentState = off;

void setup(){
  // NO INITIALIZATION NEEDED FOR READING AN ANALOGIC-SENSOR : lightSensor
  pinMode(10, OUTPUT); // led [Digital Actuator]
}

void loop() {
        switch(currentState){
                case off:
                        digitalWrite(10,LOW);
                        if ((analogRead(1) < 200)) {
                                currentState = on;
                        }
                break;
                case on:
                        digitalWrite(10,HIGH);
                        if ((analogRead(1) >= 200)) {
                                currentState = off;
                        }
                break;
        }
}
