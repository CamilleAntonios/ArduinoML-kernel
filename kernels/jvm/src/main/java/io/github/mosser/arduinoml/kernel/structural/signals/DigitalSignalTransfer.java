package io.github.mosser.arduinoml.kernel.structural.signals;

import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;

public class DigitalSignalTransfer implements DigitalSignal {
    private DigitalSensor sensor;

    public DigitalSignalTransfer(DigitalSensor sensor) {
        this.sensor = sensor;
    }
    public DigitalSensor getSensor() {
        return sensor;
    }
    public void setSensor(DigitalSensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString(){
        return String.format("digitalRead(%d)", this.sensor.getPin());
    }
}
