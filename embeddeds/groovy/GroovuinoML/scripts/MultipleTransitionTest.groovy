sensor "button1" onPin 9
sensor "button2" onPin 10
actuator "led" pin 12

state "on" means led becomes high
state "off" means led becomes low
state "blink" means led becomes high   // pour illustrer un 2ᵉ état possible

initial "off"

from on to off  when button1 becomes high     // transition 1
from on to blink when button2 becomes high     // transition 2

from off to on when button1 becomes high
from blink to off when button2 becomes high

export "MultiTransitions"
