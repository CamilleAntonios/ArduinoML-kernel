package io.github.mosser.arduinoml.kernel.generator;

import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.structural.actuators.AnalogActuator;
import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.expressions.DigitalEqualOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.NotOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.BiggerAnalogOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.BiggerOrEqualAnalogOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.EqualAnalogOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.AndOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.OrOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.XorOperation;
import io.github.mosser.arduinoml.kernel.structural.sensors.AnalogSensor;
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;

import java.util.HashMap;
import java.util.Map;

public abstract class Visitor<T> {

	public abstract void visit(App app);

	public abstract void visit(State state);
	public abstract void visit(SignalTransition transition);
	public abstract void visit(TimeTransition transition);
	public abstract void visit(BasicTransition transition);
    public abstract void visit(AnalogAction analogAction);
    public abstract void visit(DigitalAction digitalAction);


	public abstract void visit(DigitalActuator actuator);
	public abstract void visit(AnalogActuator actuator);
	public abstract void visit(DigitalSensor sensor);
	public abstract void visit(AnalogSensor sensor);
	public abstract void visit(NotOperation notOp);
	public abstract void visit(DigitalEqualOperation digitalEqualOp);
	public abstract void visit(AndOperation andOp);
	public abstract void visit(OrOperation orOp);
	public abstract void visit(XorOperation xorOp);
	public abstract void visit(EqualAnalogOperation analogEqualOp);
	public abstract void visit(BiggerAnalogOperation biggerAnalogOp);
	public abstract void visit(BiggerOrEqualAnalogOperation biggerOrEqAnalogOp);


	/***********************
	 ** Helper mechanisms **
	 ***********************/

	protected Map<String,Object> context = new HashMap<>();

	protected T result;

	public T getResult() {
		return result;
	}

}

