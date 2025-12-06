// Test 2 — Dual Check Alarm

sensor "b1" onPin 9
sensor "b2" onPin 10
actuator "buzzer" pin 11

state "alarm" means buzzer becomes high
state "idle" means buzzer becomes low

initial "idle"

// Ici on utilise la logique digitale : (b1 == HIGH) and (b2 == HIGH)
from idle to alarm when (b1.equalTo(high).and(b2.equalTo(high)))


// Retour dès qu’un bouton n’est plus HIGH
from alarm to idle when (b1.equalTo(low).or(b2.equalTo(low)))

export "DualCheckAlarm"
