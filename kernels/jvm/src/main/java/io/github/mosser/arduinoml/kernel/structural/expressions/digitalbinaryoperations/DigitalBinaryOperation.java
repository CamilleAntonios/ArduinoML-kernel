package io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations;

import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;

public abstract class DigitalBinaryOperation implements Expression {
    protected Expression left;
    protected Expression right;

    public DigitalBinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() { return left; }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() { return right; }

    public void setRight(Expression right) {
        this.right = right;
    }
}
