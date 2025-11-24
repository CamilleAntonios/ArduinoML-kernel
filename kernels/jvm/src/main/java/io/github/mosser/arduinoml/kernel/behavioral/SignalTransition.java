package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.signals.DIGITAL_SIGNAL;
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;

public class SignalTransition extends Transition {

    private DigitalSensor sensor;
    private DIGITAL_SIGNAL value;


    public DigitalSensor getSensor() {
        return sensor;
    }

    public void setSensor(DigitalSensor sensor) {
        this.sensor = sensor;
    }

    public DIGITAL_SIGNAL getValue() {
        return value;
    }

    public void setValue(DIGITAL_SIGNAL value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
