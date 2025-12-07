// --- Sensors ---
analogSensor "temperature" pin 0
analogSensor "humidity" pin 1
analogSensor "light" pin 2

// --- Actuators ---
analogActuator "fan" pin 3
analogActuator "led" pin 4

// --- States ---
state "Idle" means fan becomes 0 and led becomes 50

state "Cooling" means fan becomes 200 and led becomes 80

state "Drying" means fan becomes 150 and led becomes 60

state "Brightening" means fan becomes 0 and led becomes 255

state "Emergency" means fan becomes 255 and led becomes 0

// Initial state
initial "Idle"

// -------------------------------
//  TRANSITIONS FROM IDLE
// -------------------------------

from "Idle" to "Emergency" when temperature.greaterThan(30).and(humidity.greaterThan(60))

from "Idle" to "Cooling" when temperature.greaterThan(25)

from "Idle" to "Drying" when humidity.greaterThan(50)

from "Idle" to "Brightening" when light.smallerThan(40)


// -------------------------------
//  TRANSITIONS FROM COOLING
// -------------------------------

from "Cooling" to "Emergency" when temperature.greaterThan(35).or(humidity.greaterThan(70))

from "Cooling" to "Idle" when temperature.smallerThan(23).and(humidity.smallerThan(45))

from "Cooling" to "Drying" when humidity.greaterThan(55)


// -------------------------------
//  TRANSITIONS FROM DRYING
// -------------------------------

from "Drying" to "Cooling" when temperature.greaterThan(27)

from "Drying" to "Idle" when temperature.smallerThan(23).and(humidity.smallerThan(45))


// -------------------------------
//  TRANSITIONS FROM BRIGHTENING
// -------------------------------

from "Brightening" to "Idle" when light.greaterThan(60)

from "Brightening" to "Emergency" when temperature.greaterThan(32).and(humidity.greaterThan(65))


// -------------------------------
//  TRANSITIONS FROM EMERGENCY
// -------------------------------

from "Emergency" to "Cooling" when temperature.smallerThan(25).and(humidity.smallerThan(50))

from "Emergency" to "Idle" when temperature.smallerThan(25).and(humidity.smallerThan(45))

from "Emergency" to "Brightening" when light.smallerThan(40)


// --- Export Wiring code ---
export "SmartClimate"
