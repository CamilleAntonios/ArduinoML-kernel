package io.github.mosser.arduinoml.kernel.structural.signals;


public class DigitalSignalConstant implements DigitalSignal {
    private DigConstant value;

    public DigitalSignalConstant(DigConstant value) {
        this.value = value;
    }

    public DigConstant getValue() { return value; }

    public void setValue(DigConstant value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value == DigConstant.HIGH ? "HIGH" : "LOW";
    }
}