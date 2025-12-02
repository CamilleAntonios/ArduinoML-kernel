package io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignal;

public class LessOrEqualAnalogOperation extends AnalogBinaryOperation {
    public LessOrEqualAnalogOperation(AnalogSignal left, AnalogSignal right) {
        super(left, right);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
