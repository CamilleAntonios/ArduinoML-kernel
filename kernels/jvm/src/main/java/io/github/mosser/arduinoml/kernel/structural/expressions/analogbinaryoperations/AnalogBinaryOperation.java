package io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations;

import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignal;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignalConstant;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;

public abstract class AnalogBinaryOperation implements Expression {
    protected AnalogSignal left;
    protected AnalogSignal right;

    public AnalogBinaryOperation(AnalogSignal left, AnalogSignal right) {
        if (left instanceof AnalogSignalConstant && right instanceof AnalogSignalConstant) {
            //Nous avons fait le choix de coder cela avec un check de type dynamique, car
            //effectuer cette vérification avec un typage statique créerait beaucoup de duplication de classes.
            throw new IllegalArgumentException("Arguments of a binary analog operation cant be both constants !");
        }
        this.left = left;
        this.right = right;
    }

    public AnalogSignal getLeft() { return left; }

    public void setLeft(AnalogSignal left) {
        this.left = left;
    }

    public AnalogSignal getRight() { return right; }

    public void setRight(AnalogSignal right) {
        this.right = right;
    }
}
