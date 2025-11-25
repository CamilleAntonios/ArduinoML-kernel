package io.github.mosser.arduinoml.kernel.structural.signals;

public class AnalogSignalConstant extends AnalogSignal {
    private int value;

    public AnalogSignalConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return ""+this.value;
    }
}
