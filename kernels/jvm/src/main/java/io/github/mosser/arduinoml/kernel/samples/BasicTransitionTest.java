package io.github.mosser.arduinoml.kernel.samples;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;

import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;

import io.github.mosser.arduinoml.kernel.structural.expressions.*;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.*;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalTransfer;

import java.util.Arrays;

public class BasicTransitionTest {

    public static void main(String[] args) {

        /***************************************************
         *  BRICKS
         ***************************************************/
        DigitalSensor button = new DigitalSensor();
        button.setName("button");
        button.setPin(9);

        DigitalActuator led = new DigitalActuator();
        led.setName("led");
        led.setPin(12);


        /***************************************************
         *  STATES
         ***************************************************/
        State idle = new State();
        idle.setName("idle");

        State checking = new State();
        checking.setName("checking");

        State alert = new State();
        alert.setName("alert");


        /***************************************************
         *  ACTIONS (LED ON/OFF)
         ***************************************************/
        DigitalAction ledOn = new DigitalAction();
        ledOn.setActuator(led);
        ledOn.setValue(DigitalSignalConstant.HIGH);

        DigitalAction ledOff = new DigitalAction();
        ledOff.setActuator(led);
        ledOff.setValue(DigitalSignalConstant.LOW);

        idle.setActions(Arrays.asList(ledOff));
        checking.setActions(Arrays.asList(ledOn));
        alert.setActions(Arrays.asList(ledOn));


        /***************************************************
         *  EXPRESSIONS
         ***************************************************/
        // button == HIGH
        DigitalSignalTransfer buttonValue = new DigitalSignalTransfer(button);
        DigitalEqualOperation exprSimple = new DigitalEqualOperation(buttonValue, DigitalSignalConstant.HIGH);

        // !(button == LOW)
        NotOperation exprNot = new NotOperation(new DigitalEqualOperation(buttonValue, DigitalSignalConstant.LOW));

        // (button == HIGH) && !(button == LOW)
        AndOperation exprAnd = new AndOperation(exprSimple, exprNot);

        // (button == HIGH) ^ (button == LOW)
        XorOperation exprXor = new XorOperation(
                new DigitalEqualOperation(buttonValue, DigitalSignalConstant.HIGH),
                new DigitalEqualOperation(buttonValue, DigitalSignalConstant.LOW)
        );


        /***************************************************
         *  BASIC TRANSITIONS
         ***************************************************/
        BasicTransition idle_to_checking = new BasicTransition();
        idle_to_checking.setNext(checking);
        idle_to_checking.setCondition(exprSimple);

        BasicTransition checking_to_alert = new BasicTransition();
        checking_to_alert.setNext(alert);
        checking_to_alert.setCondition(exprAnd);

        BasicTransition alert_to_idle = new BasicTransition();
        alert_to_idle.setNext(idle);
        alert_to_idle.setCondition(exprXor);

        idle.setTransition(idle_to_checking);
        checking.setTransition(checking_to_alert);
        alert.setTransition(alert_to_idle);


        /***************************************************
         *  APPLICATION
         ***************************************************/
        App app = new App();
        app.setName("BasicTransitionTest");
        app.setBricks(Arrays.asList(button, led));
        app.setStates(Arrays.asList(idle, checking, alert));
        app.setInitial(idle);

        Visitor gen = new ToWiring();
        app.accept(gen);

        System.out.println(gen.getResult());
    }
}
