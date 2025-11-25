package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.signals.DIGITAL_SIGNAL;

public class Action implements Visitable {

	private DIGITAL_SIGNAL value;
	private DigitalActuator actuator;


	public DIGITAL_SIGNAL getValue() {
		return value;
	}

	public void setValue(DIGITAL_SIGNAL value) {
		this.value = value;
	}

	public DigitalActuator getActuator() {
		return actuator;
	}

	public void setActuator(DigitalActuator actuator) {
		this.actuator = actuator;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
