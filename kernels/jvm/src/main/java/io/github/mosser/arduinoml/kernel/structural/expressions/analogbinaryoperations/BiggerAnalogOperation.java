package io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations;

import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.DigitalBinaryOperation;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignal;

public class BiggerAnalogOperation extends AnalogBinaryOperation {
    public BiggerAnalogOperation(AnalogSignal left, AnalogSignal right) {
        super(left, right);
    }
}
