// Test 4 — Multi-State Alarm

sensor "button" onPin 9
actuator "buzzer" pin 11
actuator "led" pin 12

state "buzzing" means buzzer becomes high and led becomes low
state "lighting" means led becomes high and buzzer becomes low
state "idle" means buzzer becomes low and led becomes low

initial "idle"

// idle → buzzing
from idle to buzzing when button becomes high
// buzzing → lighting
from buzzing to lighting when button becomes high
// lighting → idle
from lighting to idle when button becomes high

export "MultiStateAlarm"
