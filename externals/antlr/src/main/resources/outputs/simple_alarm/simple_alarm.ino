// Wiring code generated from an ArduinoML model
// Application name: simpleAlarm

long debounce = 200;

enum STATE {off, on};
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
                        digitalWrite(10,HIGH);
                        digitalWrite(11,HIGH);
                        if ((digitalRead(9) == LOW)) {
                                currentState = off;
                        }
                break;
        }
}
