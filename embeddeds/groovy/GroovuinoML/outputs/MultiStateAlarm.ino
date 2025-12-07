// Wiring code generated from an ArduinoML model
// Application name: MultiStateAlarm

long debounce = 200;

enum STATE {buzzing, lighting, idle};
STATE currentState = idle;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button [Digital Sensor]
  pinMode(11, OUTPUT); // buzzer [Digital Actuator]
  pinMode(12, OUTPUT); // led [Digital Actuator]
}

void loop() {
        switch(currentState){
                case buzzing:
                        digitalWrite(11,HIGH);
                        digitalWrite(12,LOW);
                        if ((digitalRead(9) == HIGH)) {
                                currentState = lighting;
                        }
                break;
                case lighting:
                        digitalWrite(12,HIGH);
                        digitalWrite(11,LOW);
                        if ((digitalRead(9) == HIGH)) {
                                currentState = idle;
                        }
                break;
                case idle:
                        digitalWrite(11,LOW);
                        digitalWrite(12,LOW);
                        if ((digitalRead(9) == HIGH)) {
                                currentState = buzzing;
                        }
                break;
        }
}