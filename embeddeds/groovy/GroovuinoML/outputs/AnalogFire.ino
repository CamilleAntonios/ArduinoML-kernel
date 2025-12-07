// Wiring code generated from an ArduinoML model
// Application name: fireDetection

long debounce = 200;

enum STATE {Idle, Alert};
STATE currentState = Idle;

void setup(){
  // NO INITIALIZATION NEEDED FOR READING AN ANALOGIC-SENSOR : temperature
  pinMode(3, OUTPUT); // alarm [Analogic Actuator]
}

void loop() {
        switch(currentState){
                case Idle:
                        analogWrite(3,0);
                        if ((analogRead(0) > 57)) {
                                currentState = Alert;
                        }
                break;
                case Alert:
                        analogWrite(3,255);
                        if ((57 > analogRead(0))) {
                                currentState = Idle;
                        }
                break;
        }
}