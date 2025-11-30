package io.github.mosser.arduinoml.kernel.samples;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;

import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;
import io.github.mosser.arduinoml.kernel.structural.sensors.AnalogSensor;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignalConstant;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignalTransfer;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;

import io.github.mosser.arduinoml.kernel.structural.expressions.*;
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.BiggerAnalogOperation;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalTransfer;

import java.util.Arrays;

public class MultipleTransitionsTest {

    public static void main(String[] args) {

        /***************************************************
         *  BRICKS
         ***************************************************/
        DigitalSensor button = new DigitalSensor();
        button.setName("button");
        button.setPin(9);

        AnalogSensor temp = new AnalogSensor();
        temp.setName("tempSensor");
        temp.setPin(0);  // A0

        DigitalActuator buzzer = new DigitalActuator();
        buzzer.setName("buzzer");
        buzzer.setPin(12);


        /***************************************************
         *  STATES
         ***************************************************/
        State idle = new State();
        idle.setName("idle");

        State alarm = new State();
        alarm.setName("alarm");

        State cooldown = new State();
        cooldown.setName("cooldown");


        /***************************************************
         *  ACTIONS
         ***************************************************/
        DigitalAction buzzerOn = new DigitalAction();
        buzzerOn.setActuator(buzzer);
        buzzerOn.setValue(DigitalSignalConstant.HIGH);

        DigitalAction buzzerOff = new DigitalAction();
        buzzerOff.setActuator(buzzer);
        buzzerOff.setValue(DigitalSignalConstant.LOW);

        idle.setActions(Arrays.asList(buzzerOff));
        alarm.setActions(Arrays.asList(buzzerOn));
        cooldown.setActions(Arrays.asList(buzzerOff));


        /***************************************************
         *  EXPRESSIONS
         ***************************************************/
        // button == HIGH
        DigitalSignalTransfer buttonValue = new DigitalSignalTransfer(button);
        DigitalEqualOperation buttonHigh = new DigitalEqualOperation(buttonValue, DigitalSignalConstant.HIGH);

        // button == LOW
        DigitalEqualOperation buttonLow = new DigitalEqualOperation(buttonValue, DigitalSignalConstant.LOW);
        AnalogSignalTransfer tempValue = new AnalogSignalTransfer(temp);

        // analog constant 57°C
        AnalogSignalConstant threshold57 = new AnalogSignalConstant(57);
        // tempSensor > 57
        BiggerAnalogOperation tooHot = new BiggerAnalogOperation(tempValue, threshold57);
        // If your analog operations require objects instead of strings,
        // adapt to your API: (temp, "57") or (new AnalogValue(temp), new Constant("57"))


        /***************************************************
         *  MULTIPLE TRANSITIONS on idle
         ***************************************************/
        // idle -> alarm (button HIGH)
        BasicTransition idle_to_alarm = new BasicTransition();
        idle_to_alarm.setNext(alarm);
        idle_to_alarm.setCondition(buttonHigh);

        // idle -> cooldown (temp > 57°C)
        BasicTransition idle_to_cooldown = new BasicTransition();
        idle_to_cooldown.setNext(cooldown);
        idle_to_cooldown.setCondition(tooHot);

        idle.addTransition(idle_to_alarm);
        idle.addTransition(idle_to_cooldown);


        /***************************************************
         *  OTHER TRANSITIONS
         ***************************************************/
        // alarm -> idle when button LOW
        BasicTransition alarm_to_idle = new BasicTransition();
        alarm_to_idle.setNext(idle);
        alarm_to_idle.setCondition(buttonLow);
        alarm.addTransition(alarm_to_idle);

        // cooldown -> idle after 2s
        TimeTransition cooldown_to_idle = new TimeTransition();
        cooldown_to_idle.setNext(idle);
        cooldown_to_idle.setDelay(2000);
        cooldown.addTransition(cooldown_to_idle);


        /***************************************************
         *  APPLICATION
         ***************************************************/
        App app = new App();
        app.setName("MultipleTransitionsTest");
        app.setBricks(Arrays.asList(button, temp, buzzer));
        app.setStates(Arrays.asList(idle, alarm, cooldown));
        app.setInitial(idle);

        Visitor gen = new ToWiring();
        app.accept(gen);

        System.out.println(gen.getResult());
    }
}
