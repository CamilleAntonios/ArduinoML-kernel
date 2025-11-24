package io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations;

import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;

public abstract class DigitalBinaryOperation implements Expression {
    protected Expression left;
    protected Expression right;

    public DigitalBinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}
