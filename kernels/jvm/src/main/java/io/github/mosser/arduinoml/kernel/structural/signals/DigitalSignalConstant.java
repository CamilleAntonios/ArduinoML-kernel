package io.github.mosser.arduinoml.kernel.structural.signals;

public enum DigitalSignalConstant implements DigitalSignal {
    HIGH,
    LOW;

    @Override
    public String toString(){
        return this == HIGH ? "HIGH" : "LOW";
    }

}