package io.github.mosser.arduinoml.externals.antlr;

import io.github.mosser.arduinoml.externals.antlr.grammar.*;


import io.github.mosser.arduinoml.externals.antlr.grammar.ArduinomlParser;
import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.actuators.AnalogActuator;
import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator;
import io.github.mosser.arduinoml.kernel.structural.expressions.DigitalEqualOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.Expression;
import io.github.mosser.arduinoml.kernel.structural.expressions.NotOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.*;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.AndOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.OrOperation;
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.XorOperation;
import io.github.mosser.arduinoml.kernel.structural.sensors.AnalogSensor;
import io.github.mosser.arduinoml.kernel.structural.signals.*;
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor;

import java.util.*;

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

    private Map<String, DigitalSensor>   digitalSensors   = new HashMap<>();
    private Map<String, AnalogSensor>   analogSensors   = new HashMap<>();
    private Map<String, DigitalActuator> digitalActuators = new HashMap<>();
    private Map<String, AnalogActuator> analogActuators = new HashMap<>();
    private Map<String, State>    states  = new HashMap<>();
    private Map<String, List<Binding>>  bindings  = new HashMap<>();

    private class Binding { // used to support state resolution for transitions
        String to; // name of the next state, as its instance might not have been compiled yet
        Expression expression;
    }

    private State currentState = null;

    //only one of those two is available at once
    private Optional<DigitalAction> currentDigitalAction = Optional.empty();
    private Optional<AnalogAction>  currentAnalogAction = Optional.empty();

    private Binding currentTransitionBinding = null;

    private Stack<Expression> expressionStack = new Stack<>();
    private Stack<DigitalSignal> digitalSignalsStack = new Stack<>();
    private Stack<AnalogSignal> analogSignalsStack = new Stack<>();

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
        bindings.forEach((key, bindings) ->  {
            for(Binding binding : bindings) {
                BasicTransition t = new BasicTransition();
                t.setCondition(binding.expression);
                t.setNext(states.get(binding.to));
                states.get(key).addTransition(t);
            }
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
        digitalSensors.put(sensor.getName(), sensor); //TODO : changer ça ? car tous les sensors sont stockés de la même manière
    }

    @Override
    public void enterDigitalActuator(ArduinomlParser.DigitalActuatorContext ctx) {
        DigitalActuator actuator = new DigitalActuator();
        actuator.setName(ctx.actuator().location().id.getText());
        actuator.setPin(Integer.parseInt(ctx.actuator().location().port.getText()));
        this.theApp.getBricks().add(actuator);
        digitalActuators.put(actuator.getName(), actuator);//TODO : changer ça ? car tous les actuators sont stockés de la même manière
    }

    @Override
    public void enterAnalogSensor(ArduinomlParser.AnalogSensorContext ctx) {
        AnalogSensor sensor = new AnalogSensor();
        sensor.setName(ctx.sensor().location().id.getText());
        sensor.setPin(Integer.parseInt(ctx.sensor().location().port.getText()));
        this.theApp.getBricks().add(sensor);
        analogSensors.put(sensor.getName(), sensor); //TODO : changer ça ? car tous les sensors sont stockés de la même manière
    }

    @Override
    public void enterAnalogActuator(ArduinomlParser.AnalogActuatorContext ctx) {
        AnalogActuator actuator = new AnalogActuator();
        actuator.setName(ctx.actuator().location().id.getText());
        actuator.setPin(Integer.parseInt(ctx.actuator().location().port.getText()));
        this.theApp.getBricks().add(actuator);
        analogActuators.put(actuator.getName(), actuator);//TODO : changer ça ? car tous les actuators sont stockés de la même manière
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
    public void enterAction(ArduinomlParser.ActionContext ctx) {
        String receiverName =  ctx.receiver.getText();
        if(digitalActuators.containsKey(receiverName)) {
            //we can consider the action is a DigitalAction
            DigitalAction action = new DigitalAction();
            action.setActuator(digitalActuators.get(receiverName));
            this.currentDigitalAction = Optional.of(action);
        }
        else {
            //we will consider the action is an AnalogicAction
            AnalogAction action = new AnalogAction();
            action.setActuator(analogActuators.get(receiverName));
            this.currentAnalogAction = Optional.of(action);
        }
    }

    @Override
    public void enterDigitalSignal(ArduinomlParser.DigitalSignalContext ctx) {
        try {
            String textValue=ctx.DIGITAL_SIGNAL_CONST().getSymbol().getText();
            if (textValue.equalsIgnoreCase("HIGH") || textValue.equalsIgnoreCase("LOW")) {
                DigitalSignalConstant value = DigitalSignalConstant.valueOf(textValue.toUpperCase());
                if (this.currentDigitalAction.isPresent()) {
                    this.currentDigitalAction.get().setValue(value);
                }
                else {//le cas où on est dans une expression
                    digitalSignalsStack.add(value);
                }
            }
        }
        catch(Exception e) {
            //ne rien faire si ce n'est pas un digital signal constant
        }
    }

    @Override
    public void exitSignalRead(ArduinomlParser.SignalReadContext ctx) {
        String sensorName = ctx.sensor_name.getText();
        if(digitalSensors.containsKey(sensorName)) {
            DigitalSignalTransfer signalRead = new DigitalSignalTransfer(digitalSensors.get(sensorName));
            if (this.currentDigitalAction.isPresent()) {
                this.currentDigitalAction.get().setValue(signalRead);
            }
            else {//le cas où on est dans une expression
                digitalSignalsStack.add(signalRead);
            }
        }
        else {//il faut que ce soit un analog read
            AnalogSignalTransfer signalRead = new AnalogSignalTransfer(analogSensors.get(sensorName));
            if (this.currentAnalogAction.isPresent()) {
                this.currentAnalogAction.get().setValue(signalRead);
            }
            else {//le cas où on est dans une expression
                analogSignalsStack.add(signalRead);
            }
        }
    }

    @Override
    public void exitAction(ArduinomlParser.ActionContext ctx) {
        currentState.getActions().add
                (this.currentDigitalAction.isPresent() ? this.currentDigitalAction.get() : this.currentAnalogAction.get());
        this.currentDigitalAction = Optional.empty();
        this.currentAnalogAction = Optional.empty();
    }

    @Override
    public void enterAnalogSignal(ArduinomlParser.AnalogSignalContext ctx) {
        try {
            String textValue=ctx.ANALOG_SIGNAL_CONST().getSymbol().getText();
            int parsedConst = Integer.parseInt(textValue);
            AnalogSignal constantSignal = new AnalogSignalConstant(parsedConst);
            if (this.currentAnalogAction.isPresent()) {
                this.currentAnalogAction.get().setValue(constantSignal);
            }
            else {//le cas où on est dans une expr
                analogSignalsStack.add(constantSignal);
            }
        }
        catch (Exception e) {
            //do nothing. If the number can't be parsed it will be treated as an AnalogSignalRead
        }
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
        this.currentTransitionBinding.expression = expressionStack.pop();

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
    public void exitExpr(ArduinomlParser.ExprContext ctx) {
        if(ctx.getChild(0).getText().equalsIgnoreCase("not")) {
            //le seul cas où l'opérateur est présent à l'index 0. Dans tous les autres cas, il l'est à l'index 1
            expressionStack.push(new NotOperation(expressionStack.pop()));
        }
        else if(ctx.getChild(0).getText().equalsIgnoreCase("(")) {
            //si l'expression contient une parenthèse, alors la réelle condition à l'intérieure serait traitée plus tard
            //car on a la règle de grammaire expr : '(' expr ')' ;
            return;
        }
        else if (ctx.children.size() > 1){//vérification pour ne jamais rencontrer d'exceptions
            Expression left, right;
            switch (ctx.getChild(1).getText().toLowerCase()) {
                case "and":
                    right=expressionStack.pop();
                    left=expressionStack.pop();
                    expressionStack.push(new AndOperation(left, right));
                    break;
                case "or":
                    right=expressionStack.pop();
                    left=expressionStack.pop();
                    expressionStack.push(new OrOperation(left, right));
                    break;
                case "xor":
                    right=expressionStack.pop();
                    left=expressionStack.pop();
                    expressionStack.push(new XorOperation(left, right));
                    break;
            }
            //no default case : if it did not match a case, it is a analog comp or digital equal and is treated
        }
    }

    @Override
    public void exitDigitalEqualComp(ArduinomlParser.DigitalEqualCompContext ctx) {
        DigitalSignal right = digitalSignalsStack.pop();
        DigitalSignal left = digitalSignalsStack.pop();
        expressionStack.push(new DigitalEqualOperation(left, right));//values will be set on exit
    }

    @Override
    public void exitAnalogComp(ArduinomlParser.AnalogCompContext ctx) {
        AnalogSignal right = analogSignalsStack.pop();
        AnalogSignal left = analogSignalsStack.pop();
        switch(ctx.getChild(1).getText()) {
            case "<":
                expressionStack.push(new LessAnalogOperation(left, right));//values will be set on exit
                break;
            case "<=":
                expressionStack.push(new LessOrEqualAnalogOperation(left, right));//values will be set on exit
                break;
            case ">":
                expressionStack.push(new BiggerAnalogOperation(left, right));//values will be set on exit
                break;
            case ">=":
                expressionStack.push(new BiggerOrEqualAnalogOperation(left, right));//values will be set on exit
                break;
            default://cas "=="
                expressionStack.push(new EqualAnalogOperation(left, right));//values will be set on exit
                break;
        }
    }

    @Override
    public void enterInitial(ArduinomlParser.InitialContext ctx) {
        this.theApp.setInitial(this.currentState);
    }

}

