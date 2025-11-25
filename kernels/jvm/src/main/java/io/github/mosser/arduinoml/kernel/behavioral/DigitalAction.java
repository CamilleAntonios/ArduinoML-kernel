package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignal;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;

public class DigitalAction extends Action implements Visitable {

    DigitalSignal digitalSignal;
    DigitalActuator digitalActuator;

    public DigitalSignal getValue() {
        return digitalSignal;
    }


    public void setValue(DigitalSignal value) {
        this.digitalSignal = value;
    }

    public DigitalActuator getActuator() {
        return digitalActuator;
    }

    public void setActuator(DigitalActuator actuator) {
        this.digitalActuator = actuator;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
