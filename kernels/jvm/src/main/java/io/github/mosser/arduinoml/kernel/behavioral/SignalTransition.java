package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;

public class SignalTransition extends Transition {

    private DigitalSensor sensor;
    private DigitalSignalConstant value;


    public DigitalSensor getSensor() {
        return sensor;
    }

    public void setSensor(DigitalSensor sensor) {
        this.sensor = sensor;
    }

    public DigitalSignalConstant getValue() {
        return value;
    }

    public void setValue(DigitalSignalConstant value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
