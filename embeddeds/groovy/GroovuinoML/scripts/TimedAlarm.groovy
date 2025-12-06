// Test 5 — Timed + Digital Transition

sensor "button" onPin 9
actuator "led" pin 12
actuator "buzzer" pin 11

state "active" means led becomes high and buzzer becomes high
state "standby" means led becomes low and buzzer becomes low

initial "standby"

from standby to active when button becomes high
from active to standby after 2000   // extinction automatique

export "TimedAlarm"
