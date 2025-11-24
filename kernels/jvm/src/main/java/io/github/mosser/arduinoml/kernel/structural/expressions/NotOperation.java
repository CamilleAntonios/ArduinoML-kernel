package io.github.mosser.arduinoml.kernel.structural.expressions;

public class NotOperation implements Expression{
    private Expression expr;
    public NotOperation(Expression expr) {
        this.expr = expr;
    }
}
