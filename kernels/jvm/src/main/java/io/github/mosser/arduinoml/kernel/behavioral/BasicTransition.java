package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;

public class BasicTransition extends Transition{
    protected Expression condition;

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression expr) {
        condition = expr;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
