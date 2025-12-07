analogSensor "tempSensor" pin 1
sensor "button" onPin 9
analogActuator "led" pin 10


state "transmit" means led becomes tempSensor

state "halt" means led becomes 0

initial "transmit"

from "transmit" to "halt" when button.equalTo(HIGH)
from "halt" to "transmit" when button.equalTo(LOW)

export "tempToLed"
