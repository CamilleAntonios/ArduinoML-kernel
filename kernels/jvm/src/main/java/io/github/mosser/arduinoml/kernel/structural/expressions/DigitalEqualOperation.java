package io.github.mosser.arduinoml.kernel.structural.expressions;

import io.github.mosser.arduinoml.kernel.structural.signals.DIGITAL_SIGNAL;

public class DigitalEqualOperation implements Expression{
    private DIGITAL_SIGNAL left;
    private DIGITAL_SIGNAL right;
    public DigitalEqualOperation(DIGITAL_SIGNAL left, DIGITAL_SIGNAL right) {
        this.left = left;
        this.right = right;
    }
}