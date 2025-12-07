// Wiring code generated from an ArduinoML model
// Application name: multiStateBasedAlarm

long debounce = 200;

enum STATE {off, on, onSuper};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button [Digital Sensor]
  pinMode(10, OUTPUT); // led [Digital Actuator]
  pinMode(11, OUTPUT); // buzzer [Digital Actuator]
}

void loop() {
        switch(currentState){
                case off:
                        digitalWrite(10,LOW);
                        digitalWrite(11,LOW);
                        if ((digitalRead(9) == HIGH)) {
                                currentState = on;
                        }
                break;
                case on:
                        digitalWrite(11,HIGH);
                        if ((digitalRead(9) == HIGH)) {
                                currentState = onSuper;
                        }
                break;
                case onSuper:
                        digitalWrite(11,LOW);
                        digitalWrite(10,HIGH);
                        if ((digitalRead(9) == HIGH)) {
                                currentState = off;
                        }
                break;
        }
}
