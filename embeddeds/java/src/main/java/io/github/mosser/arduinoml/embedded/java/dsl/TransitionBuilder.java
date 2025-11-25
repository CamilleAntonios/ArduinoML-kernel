package io.github.mosser.arduinoml.embedded.java.dsl;


import io.github.mosser.arduinoml.kernel.behavioral.SignalTransition;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;

public class TransitionBuilder {


    private TransitionTableBuilder parent;

    private SignalTransition local;

    TransitionBuilder(TransitionTableBuilder parent, String source) {
        this.parent = parent;
        this.local = new SignalTransition();
        parent.findState(source).setTransition(local);
    }


    public TransitionBuilder when(String sensor) {
        local.setSensor(parent.findSensor(sensor));
        return this;
    }

    public TransitionBuilder isHigh() {
        local.setValue(DigitalSignalConstant.HIGH);
        return this;
    }

    public TransitionBuilder isLow() {
        local.setValue(DigitalSignalConstant.LOW);
        return this;
    }

    public TransitionTableBuilder goTo(String state) {
        local.setNext(parent.findState(state));
        return parent;
    }


}
