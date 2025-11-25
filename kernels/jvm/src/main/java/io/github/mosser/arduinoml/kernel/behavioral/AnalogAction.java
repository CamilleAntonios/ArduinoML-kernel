package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.actuators.AnalogActuator;
import io.github.mosser.arduinoml.kernel.structural.actuators.AnalogActuator;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignal;

public class AnalogAction extends Action implements Visitable {

    AnalogSignal analogSignal;
    AnalogActuator analogActuator;


    public AnalogSignal getValue() {
        return analogSignal;
    }


    public void setValue(AnalogSignal value) {
        this.analogSignal = value;
    }

    public AnalogActuator getActuator() {
        return analogActuator;
    }

    public void setActuator(AnalogActuator actuator) {
        this.analogActuator = actuator;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
