package io.github.mosser.arduinoml.kernel.structural.signals;

import io.github.mosser.arduinoml.kernel.structural.sensors.AnalogSensor;

public class AnalogSignalTransfer extends AnalogSignal {
    private AnalogSensor sensor;

    public AnalogSignalTransfer(AnalogSensor sensor) {
        this.sensor = sensor;
    }

    public AnalogSensor getSensor() {
        return sensor;
    }
    public void setSensor(AnalogSensor sensor) {
        this.sensor = sensor;
    }
}
