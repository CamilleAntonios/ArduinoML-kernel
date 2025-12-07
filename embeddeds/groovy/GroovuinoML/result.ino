// Wiring code generated from an ArduinoML model
// Application name: VerySimpleAlarm

long debounce = 200;

enum STATE {on, off};
STATE currentState = off;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button [Digital Sensor]
  pinMode(12, OUTPUT); // led [Digital Actuator]
  pinMode(11, OUTPUT); // buzzer [Digital Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			digitalWrite(11,HIGH);
			if ((digitalRead(9) == LOW)) {
				currentState = off;
			}
		break;
		case off:
			digitalWrite(12,LOW);
			digitalWrite(11,LOW);
			if ((digitalRead(9) == HIGH)) {
				currentState = on;
			}
		break;
	}
}
