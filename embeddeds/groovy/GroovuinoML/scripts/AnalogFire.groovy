analogSensor "temperature" pin 0
analogActuator "alarm" pin 3

state "Idle" means alarm becomes 0
state "Alert" means alarm becomes 255

initial "Idle"

from "Idle" to "Alert" when temperature.greaterThan(57)
from "Idle" to "Alert" when temperature.smallerThan(57)

export "fireDetection"
