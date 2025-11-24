package io.github.mosser.arduinoml.kernel.structural.expressions;

public class DigitalValue implements Expression{
    private boolean value;
    public DigitalValue(boolean value) {
        this.value = value;
    }
}