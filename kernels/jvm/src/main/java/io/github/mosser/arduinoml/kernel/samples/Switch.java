package io.github.mosser.arduinoml.kernel.samples;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.behavioral.State;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.expressions.DigitalEqualOperation;
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalTransfer;

import java.util.Arrays;

public class Switch {

	public static void main(String[] args) {

		// Declaring elementary bricks
		DigitalSensor button = new DigitalSensor();
		button.setName("button");
		button.setPin(9);
		DigitalSignalTransfer buttonValue = new DigitalSignalTransfer(button);

		DigitalActuator led = new DigitalActuator();
		led.setName("LED");
		led.setPin(12);

		// Declaring states
		State on = new State();
		on.setName("on");

		State off = new State();
		off.setName("off");

		// Creating actions
		DigitalAction switchTheLightOn = new DigitalAction();
		switchTheLightOn.setActuator(led);
		switchTheLightOn.setValue(DigitalSignalConstant.HIGH);

		DigitalAction switchTheLightOff = new DigitalAction();
		switchTheLightOff.setActuator(led);
		switchTheLightOff.setValue(DigitalSignalConstant.LOW);

		// Binding actions to states
		on.setActions(Arrays.asList(switchTheLightOn));
		off.setActions(Arrays.asList(switchTheLightOff));

		// Creating transitions
		BasicTransition on2off = new BasicTransition();
		on2off.setNext(off);
		on2off.setCondition(new DigitalEqualOperation(buttonValue, DigitalSignalConstant.LOW));


		BasicTransition off2on = new BasicTransition();
		off2on.setNext(on);
		off2on.setCondition(new DigitalEqualOperation(buttonValue, DigitalSignalConstant.HIGH));


		// Binding transitions to states
		on.addTransition(on2off);
		off.addTransition(off2on);

		// Building the App
		App theSwitch = new App();
		theSwitch.setName("Switch!");
		theSwitch.setBricks(Arrays.asList(button, led ));
		theSwitch.setStates(Arrays.asList(on, off));
		theSwitch.setInitial(off);

		// Generating Code
		Visitor codeGenerator = new ToWiring();
		theSwitch.accept(codeGenerator);

		// Printing the generated code on the console
		System.out.println(codeGenerator.getResult());
	}

}
