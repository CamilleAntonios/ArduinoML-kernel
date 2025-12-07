// Wiring code generated from an ArduinoML model
// Application name: dualCheckAlarm

long debounce = 200;

enum STATE {off, on};
STATE currentState = off;

boolean firstButtonBounceGuard = false;
long firstButtonLastDebounceTime = 0;

boolean secondButtonBounceGuard = false;
long secondButtonLastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // firstButton [Digital Sensor]
  pinMode(10, INPUT);  // secondButton [Digital Sensor]
  pinMode(11, OUTPUT); // buzzer [Digital Actuator]
}

void loop() {
        switch(currentState){
                case off:
                        digitalWrite(11,LOW);
                        if (((digitalRead(9) == HIGH) && (digitalRead(10) == HIGH))) {
                                currentState = on;
                        }
                break;
                case on:
                        digitalWrite(11,HIGH);
                        if (((digitalRead(9) == LOW) || (digitalRead(10) == LOW))) {
                                currentState = off;
                        }
                break;
        }
}
