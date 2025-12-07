// Wiring code generated from an ArduinoML model
// Application name: tempToLed

long debounce = 200;

enum STATE {transmit, halt};
STATE currentState = transmit;

boolean buttonBounceGuard = false;
long buttonLastDebounceTime = 0;

void setup(){
  // NO INITIALIZATION NEEDED FOR READING AN ANALOGIC-SENSOR : tempSensor 
  pinMode(9, INPUT);  // button [Digital Sensor]
  pinMode(10, OUTPUT); // led [Analogic Actuator]
}

void loop() {
	switch(currentState){
		case transmit:
			analogWrite(10,analogRead(1));
			if ((digitalRead(9) == HIGH)) {
				currentState = halt;
			}
		break;
		case halt:
			analogWrite(10,0);
			if ((digitalRead(9) == LOW)) {
				currentState = transmit;
			}
		break;
	}
}
