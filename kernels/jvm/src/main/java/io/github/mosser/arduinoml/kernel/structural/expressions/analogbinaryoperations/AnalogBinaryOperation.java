package io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignal;

public abstract class AnalogBinaryOperation implements Expression {
    protected AnalogSignal left;
    protected AnalogSignal right;

    public AnalogBinaryOperation(AnalogSignal left, AnalogSignal right) {
        this.left = left;
        this.right = right;
    }

    public AnalogSignal getLeft() { return left; }

    public void setLeft(AnalogSignal left) {
        this.left = left;
    }

    public AnalogSignal getRight() { return right; }

    public void setRight(AnalogSignal right) {
        this.right = right;
    }
}
