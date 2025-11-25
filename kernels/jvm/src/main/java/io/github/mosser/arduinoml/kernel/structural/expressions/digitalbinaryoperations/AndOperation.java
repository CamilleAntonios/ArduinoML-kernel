package io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;

public class AndOperation extends DigitalBinaryOperation {
    public AndOperation(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
