// Wiring code generated from an ArduinoML model
// Application name: SmartClimate

long debounce = 200;

enum STATE {Idle, Cooling, Drying, Brightening, Emergency};
STATE currentState = Idle;

void setup(){
  // NO INITIALIZATION NEEDED FOR READING AN ANALOGIC-SENSOR : temperature 
  // NO INITIALIZATION NEEDED FOR READING AN ANALOGIC-SENSOR : humidity 
  // NO INITIALIZATION NEEDED FOR READING AN ANALOGIC-SENSOR : light 
  pinMode(3, OUTPUT); // fan [Analogic Actuator]
  pinMode(4, OUTPUT); // led [Analogic Actuator]
}

void loop() {
	switch(currentState){
		case Idle:
			analogWrite(3,0);
			analogWrite(4,50);
		break;
		case Cooling:
			analogWrite(3,200);
			analogWrite(4,80);
		break;
		case Drying:
			analogWrite(3,150);
			analogWrite(4,60);
		break;
		case Brightening:
			analogWrite(3,0);
			analogWrite(4,255);
		break;
		case Emergency:
			analogWrite(3,255);
			analogWrite(4,0);
		break;
	}
}
