// Wiring code generated from an ArduinoML model
// Application name: StateBasedAlarm

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button [Digital Sensor]
  pinMode(12, OUTPUT); // led [Digital Actuator]
}

void loop() {
        switch(currentState){
                case on:
                        digitalWrite(12,HIGH);
                        if ((digitalRead(9) == HIGH)) {
                                currentState = off;
                        }
                break;
                case off:
                        digitalWrite(12,LOW);
                        if ((digitalRead(9) == HIGH)) {
                                currentState = on;
                        }
                break;
        }
}