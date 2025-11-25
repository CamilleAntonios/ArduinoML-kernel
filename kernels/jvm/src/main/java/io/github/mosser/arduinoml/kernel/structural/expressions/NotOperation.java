package io.github.mosser.arduinoml.kernel.structural.expressions;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class NotOperation implements Expression {
    private Expression expr;
    public NotOperation(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    public void setExpr(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
