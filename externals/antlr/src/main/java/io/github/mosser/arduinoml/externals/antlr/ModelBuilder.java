package io.github.mosser.arduinoml.externals.antlr;

import io.github.mosser.arduinoml.externals.antlr.grammar.*;


import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.actuators.AnalogActuator;
import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.expressions.DigitalEqualOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.OrOperation;
import io.github.mosser.arduinoml.kernel.structural.sensors.AnalogSensor;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignalConstant;
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignalTransfer;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant;
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalTransfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelBuilder extends ArduinomlBaseListener {

    /********************
     ** Business Logic **
     ********************/

    private App theApp = null;
    private boolean built = false;

    public App retrieve() {
        if (built) { return theApp; }
        throw new RuntimeException("Cannot retrieve a model that was not created!");
    }

    /*******************
     ** Symbol tables **
     *******************/

    private Map<String, DigitalSensor>   sensors   = new HashMap<>();
    private Map<String, DigitalActuator> actuators = new HashMap<>();
    private Map<String, State>    states  = new HashMap<>();
    private Map<String, List<Binding>>  bindings  = new HashMap<>();

    private class Binding { // used to support state resolution for transitions
        String to; // name of the next state, as its instance might not have been compiled yet
        Expression expression;
    }

    private State currentState = null;
    private DigitalAction currentDigitalAction = null;
    private AnalogAction  currentAnalogAction = null;
    private Binding currentTransitionBinding = null;

    /**************************
     ** Listening mechanisms **
     **************************/

    @Override
    public void enterRoot(ArduinomlParser.RootContext ctx) {
        built = false;
        theApp = new App();
    }

    @Override public void exitRoot(ArduinomlParser.RootContext ctx) {
        // Resolving states in transitions
        bindings.forEach((key, binding) ->  {
            BasicTransition t = new BasicTransition();
            DigitalSignalTransfer triggerValue = new DigitalSignalTransfer(binding.trigger);
            t.setCondition(new DigitalEqualOperation(triggerValue, binding.value));
            t.setNext(states.get(binding.to));
            states.get(key).setTransition(t);
        });
        this.built = true;
    }

    @Override
    public void enterDeclaration(ArduinomlParser.DeclarationContext ctx) {
        theApp.setName(ctx.name.getText());
    }

    @Override
    public void enterDigitalSensor(ArduinomlParser.DigitalSensorContext ctx) {
        DigitalSensor sensor = new DigitalSensor();
        sensor.setName(ctx.sensor().location().id.getText());
        sensor.setPin(Integer.parseInt(ctx.sensor().location().port.getText()));
        this.theApp.getBricks().add(sensor);
        sensors.put(sensor.getName(), sensor); //TODO : changer ça ? car tous les sensors sont stockés de la même manière
    }

    @Override
    public void enterDigitalActuator(ArduinomlParser.DigitalActuatorContext ctx) {
        DigitalActuator actuator = new DigitalActuator();
        actuator.setName(ctx.actuator().location().id.getText());
        actuator.setPin(Integer.parseInt(ctx.actuator().location().port.getText()));
        this.theApp.getBricks().add(actuator);
        actuators.put(actuator.getName(), actuator);//TODO : changer ça ? car tous les actuators sont stockés de la même manière
    }

    @Override
    public void enterAnalogSensor(ArduinomlParser.AnalogSensorContext ctx) {
        AnalogSensor sensor = new AnalogSensor();
        sensor.setName(ctx.sensor().location().id.getText());
        sensor.setPin(Integer.parseInt(ctx.sensor().location().port.getText()));
        this.theApp.getBricks().add(sensor);
        sensors.put(sensor.getName(), sensor); //TODO : changer ça ? car tous les sensors sont stockés de la même manière
    }

    @Override
    public void enterAnalogActuator(ArduinomlParser.AnalogActuatorContext ctx) {
        AnalogActuator actuator = new AnalogActuator();
        actuator.setName(ctx.actuator().location().id.getText());
        actuator.setPin(Integer.parseInt(ctx.actuator().location().port.getText()));
        this.theApp.getBricks().add(actuator);
        actuators.put(actuator.getName(), actuator);//TODO : changer ça ? car tous les actuators sont stockés de la même manière
    }

    @Override
    public void enterState(ArduinomlParser.StateContext ctx) {
        State local = new State();
        local.setName(ctx.name.getText());
        this.currentState = local;
        this.states.put(local.getName(), local);
    }

    @Override
    public void exitState(ArduinomlParser.StateContext ctx) {
        this.theApp.getStates().add(this.currentState);
        this.currentState = null;
    }

    @Override
    public void enterDigitalAction(ArduinomlParser.DigitalActionContext ctx) {
        DigitalAction action = new DigitalAction();
        action.setActuator(actuators.get(ctx.receiver.getText()));
        //action.setValue(DigitalSignalConstant.valueOf(ctx.value.getText()));
        this.currentDigitalAction = action;
    }

    @Override
    public void enterDigitalSignal(ArduinomlParser.DigitalSignalContext ctx) {
        String textValue=ctx.DIGITAL_SIGNAL_CONST().getSymbol().getText();
        if (textValue.equalsIgnoreCase("HIGH") || textValue.equalsIgnoreCase("LOW")) {
            if (this.currentDigitalAction != null) {
                this.currentDigitalAction.setValue(DigitalSignalConstant.valueOf(textValue.toUpperCase()));
            }
            //TODO : faire le cas où on est dans une expression
        }
    }

    @Override
    public void enterDigitalSignalRead(ArduinomlParser.DigitalSignalReadContext ctx) {
        if (this.currentDigitalAction != null) {
            DigitalSignalTransfer signalRead = new DigitalSignalTransfer(sensors.get(ctx.digital_sensor.getText()));
            this.currentDigitalAction.setValue(signalRead);
        }
        //TODO : faire le cas où on est dans une expression
    }

    @Override
    public void exitDigitalAction(ArduinomlParser.DigitalActionContext ctx) {
        currentState.getActions().add(this.currentDigitalAction);
        this.currentDigitalAction = null;
    }

    @Override
    public void enterAnalogAction(ArduinomlParser.AnalogActionContext ctx) {
        AnalogAction action = new AnalogAction();
        action.setActuator(actuators.get(ctx.receiver.getText()));
        //action.setValue(DigitalSignalConstant.valueOf(ctx.value.getText()));
        this.currentAnalogAction = action;
    }

    @Override
    public void enterAnalogSignal(ArduinomlParser.AnalogSignalContext ctx) {
        String textValue=ctx.ANALOG_SIGNAL_CONST().getSymbol().getText();
        try {
            Integer parsedConst = Integer.parseInt(textValue);
            if (this.currentAnalogAction != null) {
                this.currentAnalogAction.setValue(new AnalogSignalConstant(parsedConst));
            }
            //todo: faire le cas où on est dans une expr
        }
        catch (NumberFormatException e) {
            //do nothing. If the number can't be parsedn it will be treated as an AnalogSignalRead
        }
    }

    @Override
    public void enterAnalogSignalRead(ArduinomlParser.AnalogSignalReadContext ctx) {
        if (this.currentAnalogAction != null) {
            AnalogSignalTransfer signalRead = new AnalogSignalTransfer(sensors.get(ctx.analog_sensor.getText()));
            this.currentAnalogAction.setValue(signalRead);
        }
        //TODO : faire le cas où on est dans une expression
    }

    @Override
    public void exitAnalogAction(ArduinomlParser.AnalogActionContext ctx) {
        currentState.getActions().add(this.currentAnalogAction);
        this.currentAnalogAction = null;
    }

    @Override
    public void enterTransition(ArduinomlParser.TransitionContext ctx) {
        // Creating a placeholder as the next state might not have been compiled yet.
        Binding toBeResolvedLater = new Binding();
        toBeResolvedLater.to      = ctx.next.getText();
        this.currentTransitionBinding= toBeResolvedLater;
    }

    @Override
    public void exitTransition(ArduinomlParser.TransitionContext ctx) {
        if (!bindings.containsKey(this.currentState.getName())) {
            List<Binding> newlyCreatedList = new ArrayList<>();
            newlyCreatedList.add(this.currentTransitionBinding);
            bindings.put(this.currentState.getName(), newlyCreatedList);
        }
        else {
            bindings.get(this.currentState.getName()).add(this.currentTransitionBinding);
        }
        this.currentTransitionBinding = null;
    }

    @Override
    public void enterInitial(ArduinomlParser.InitialContext ctx) {
        this.theApp.setInitial(this.currentState);
    }

}

