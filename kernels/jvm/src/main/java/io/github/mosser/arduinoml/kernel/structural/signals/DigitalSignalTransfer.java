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
    public void setSensor(DigialSensor sensor) {
        this.sensor = sensor;
    }
}
