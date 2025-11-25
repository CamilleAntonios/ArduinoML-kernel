package io.github.mosser.arduinoml.kernel.structural.expressions;

import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;

public class DigitalEqualOperation implements Expression{
    private DigitalSignalConstant left;
    private DigitalSignalConstant right;
    public DigitalEqualOperation(DigitalSignalConstant left, DigitalSignalConstant right) {
        this.left = left;
        this.right = right;
    }
}