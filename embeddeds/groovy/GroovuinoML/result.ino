// Wiring code generated from an ArduinoML model
// Application name: MultiTransitions

long debounce = 200;

enum STATE {on, off, blink};
STATE currentState = off;

boolean button1BounceGuard = false;
long button1LastDebounceTime = 0;

boolean button2BounceGuard = false;
long button2LastDebounceTime = 0;

void setup(){
  pinMode(9, INPUT);  // button1 [Digital Sensor]
  pinMode(10, INPUT);  // button2 [Digital Sensor]
  pinMode(12, OUTPUT); // led [Digital Actuator]
}

void loop() {
	switch(currentState){
		case on:
			digitalWrite(12,HIGH);
			if ((digitalRead(9) == HIGH)) {
				currentState = off;
			}
			if ((digitalRead(10) == HIGH)) {
				currentState = blink;
			}
		break;
		case off:
			digitalWrite(12,LOW);
			if ((digitalRead(9) == HIGH)) {
				currentState = on;
			}
		break;
		case blink:
			digitalWrite(12,HIGH);
			if ((digitalRead(10) == HIGH)) {
				currentState = off;
			}
		break;
	}
}
