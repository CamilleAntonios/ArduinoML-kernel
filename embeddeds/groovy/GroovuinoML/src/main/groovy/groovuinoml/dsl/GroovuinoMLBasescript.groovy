package main.groovy.groovuinoml.dsl


import io.github.mosser.arduinoml.kernel.behavioral.Action
import io.github.mosser.arduinoml.kernel.behavioral.DigitalAction
import io.github.mosser.arduinoml.kernel.behavioral.State
import io.github.mosser.arduinoml.kernel.structural.actuators.DigitalActuator
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.DigitalEqualOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.NotOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.Expression
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.BiggerAnalogOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.BiggerOrEqualAnalogOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.EqualAnalogOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.AndOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.OrOperation
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalTransfer
import main.groovy.groovuinoml.dsl.GroovuinoMLBinding

abstract class GroovuinoMLBasescript extends Script {
//	public static Number getDuration(Number number, TimeUnit unit) throws IOException {
//		return number * unit.inMillis;
//	}

	GroovuinoMLBasescript() {
		Expression.metaClass.and = { other -> new AndOperation(delegate, other) }
		Expression.metaClass.or = { other -> new OrOperation(delegate, other) }
		Expression.metaClass.greaterThan = { rhs -> new BiggerAnalogOperation(delegate, rhs) }
		Expression.metaClass.greaterOrEqual = { rhs -> new BiggerOrEqualAnalogOperation(delegate, rhs) }
		Expression.metaClass.equalTo = { rhs -> new EqualAnalogOperation(delegate, rhs) }
	}

	// sensor "name" pin n
	def sensor(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n) },
		onPin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n)}]
	}
	
	// actuator "name" pin n
	def actuator(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuator(name, n) }]
	}
	
	// state "name" means actuator becomes signal [and actuator becomes signal]*n
	def state(String name) {
		List<Action> actions = new ArrayList<Action>()
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions)
		// recursive closure to allow multiple and statements
		def closure
		closure = { actuator -> 
			[becomes: { signal ->
				Action action = new DigitalAction()
				action.setActuator(actuator instanceof String ? (DigitalActuator)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (DigitalActuator)actuator)
				action.setValue(signal instanceof String ? (DigitalSignalConstant)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (DigitalSignalConstant)signal)
				actions.add(action)
				[and: closure]
			}]
		}
		[means: closure]
	}
	// initial state
	def initial(state) {
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setInitialState(state instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state) : (State)state)
	}
	
	// from state1 to state2 when sensor becomes signal
	def from(state1) {
		[to: { state2 ->
			/*[when: { expr ->
				((GroovuinoMLBinding)this.getBinding())
						.getGroovuinoMLModel()
						.createTransition(
								state1 instanceof String ? ... : state1,
								state2 instanceof String ? ... : state2,
								expr
						)
			}]*/
			[when: { sensor ->
				[becomes: { signal ->
					def sensorObj = sensor instanceof String ?
							(DigitalSensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) :
							(DigitalSensor)sensor

					def signalObj = signal instanceof String ?
							(DigitalSignalConstant)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) :
							(DigitalSignalConstant)signal

					// Build an expression: sensor == signal
					def leftSignal = new DigitalSignalTransfer(sensorObj)
					def expr = new DigitalEqualOperation(leftSignal, signalObj)

					((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
							state1 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state1) : (State)state1,
							state2 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state2) : (State)state2,
							expr
					)
				}]
			},
			after: { delay ->
				((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
						state1 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state1) : (State)state1,
						state2 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state2) : (State)state2,
						delay)
			}]
		}]
	}

	// not(expr)
	def not(expr) {
		new NotOperation(expr)
	}


	// export name
	def export(String name) {
		println(((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().generateCode(name).toString())
	}
	
	// disable run method while running
	int count = 0
	abstract void scriptBody()
	def run() {
		if(count == 0) {
			count++
			scriptBody()
		} else {
			println "Run method is disabled"
		}
	}
}
