// Wiring code generated from an ArduinoML model
// Application name: DualCheckAlarm

long debounce = 200;

enum STATE {alarm, idle};
STATE currentState = idle;

boolean b1BounceGuard = false;
long b1LastDebounceTime = 0;

boolean b2BounceGuard = false;
long b2LastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // b1 [Digital Sensor]
  pinMode(10, INPUT);  // b2 [Digital Sensor]
  pinMode(11, OUTPUT); // buzzer [Digital Actuator]
}

void loop() {
	switch(currentState){
		case alarm:
			digitalWrite(11,HIGH);
			if (((digitalRead(9) == LOW) || (digitalRead(10) == LOW))) {
				currentState = idle;
			}
		break;
		case idle:
			digitalWrite(11,LOW);
			if (((digitalRead(9) == HIGH) && (digitalRead(10) == HIGH))) {
				currentState = alarm;
			}
		break;
	}
}
