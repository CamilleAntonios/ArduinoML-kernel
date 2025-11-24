package io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations;

import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignal;

public class BiggerOrEqualAnalogOperation extends AnalogBinaryOperation {
    public BiggerOrEqualAnalogOperation(AnalogSignal left, AnalogSignal right) {
        super(left, right);
    }
}
