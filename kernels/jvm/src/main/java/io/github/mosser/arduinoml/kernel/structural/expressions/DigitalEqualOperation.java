package io.github.mosser.arduinoml.kernel.structural.expressions;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignal;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;

public class DigitalEqualOperation implements Expression{
    private DigitalSignal left;
    private DigitalSignal right;
    public DigitalEqualOperation(DigitalSignal left, DigitalSignal right) {
        this.left = left;
        this.right = right;
    }

    public DigitalSignal getLeft() { return left; }

    public void setLeft(DigitalSignal left) {
        this.left = left;
    }

    public DigitalSignal getRight() { return right; }

    public void setRight(DigitalSignal right) {
        this.right = right;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}