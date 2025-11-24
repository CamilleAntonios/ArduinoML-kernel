package io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations;

import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignal;

public class EqualAnalogOperation extends AnalogBinaryOperation {
    public EqualAnalogOperation(AnalogSignal left, AnalogSignal right) {
        super(left, right);
    }
}
