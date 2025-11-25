package io.github.mosser.arduinoml.embedded.java.dsl;


import io.github.mosser.arduinoml.kernel.behavioral.Action;
import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.signals.DIGITAL_SIGNAL;

import java.util.Optional;

public class InstructionBuilder {

    private StateBuilder parent;

    private Action local =  new Action();

    InstructionBuilder(StateBuilder parent, String target) {
        this.parent = parent;
        Optional<DigitalActuator> opt = parent.parent.findActuator(target);
        DigitalActuator act = opt.orElseThrow(() -> new IllegalArgumentException("Illegal actuator: ["+target+"]"));
        local.setActuator(act);
    }

    public StateBuilder toHigh() { local.setValue(DIGITAL_SIGNAL.HIGH); return goUp(); }

    public StateBuilder toLow() { local.setValue(DIGITAL_SIGNAL.LOW); return goUp(); }

    private StateBuilder goUp() {
        parent.local.getActions().add(this.local);
        return parent;
    }

}
