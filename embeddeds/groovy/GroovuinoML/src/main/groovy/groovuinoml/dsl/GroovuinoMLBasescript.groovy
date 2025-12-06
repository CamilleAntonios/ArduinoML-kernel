package main.groovy.groovuinoml.dsl


import io.github.mosser.arduinoml.kernel.behavioral.Action
import io.github.mosser.arduinoml.kernel.behavioral.AnalogAction
import io.github.mosser.arduinoml.kernel.behavioral.DigitalAction
import io.github.mosser.arduinoml.kernel.behavioral.State
import io.github.mosser.arduinoml.kernel.structural.actuators.AnalogActuator
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.DigitalEqualOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.NotOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.Expression
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.BiggerAnalogOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.BiggerOrEqualAnalogOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.analogbinaryoperations.EqualAnalogOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.AndOperation
import io.github.mosser.arduinoml.kernel.structural.expressions.digitalbinaryoperations.OrOperation
import io.github.mosser.arduinoml.kernel.structural.sensors.DigitalSensor
import io.github.mosser.arduinoml.kernel.structural.sensors.AnalogSensor
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignalConstant
import io.github.mosser.arduinoml.kernel.structural.signals.AnalogSignalTransfer
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalConstant
import io.github.mosser.arduinoml.kernel.structural.signals.DigitalSignalTransfer
import main.groovy.groovuinoml.dsl.GroovuinoMLBinding

abstract class GroovuinoMLBasescript extends Script {
//	public static Number getDuration(Number number, TimeUnit unit) throws IOException {
//		return number * unit.inMillis;
//	}

	GroovuinoMLBasescript() {

		// Conversion universelle Number/Sensor -> AnalogSignal
		def toAnalogSignal = { obj ->
			if (obj instanceof Number) return new AnalogSignalConstant(obj)
			if (obj instanceof AnalogSensor) return new AnalogSignalTransfer(obj)
			throw new RuntimeException("Cannot convert ${obj} to AnalogSignal")
		}

		// OPERATEURS LOGIQUES SUR LES EXPRESSIONS
		Expression.metaClass.and = { other ->
			return new AndOperation(delegate, other)
		}
		Expression.metaClass.or = { other ->
			return new OrOperation(delegate, other)
		}

		// Égalité digitale
		DigitalSensor.metaClass.equalTo = { right ->
			def leftSignal = new DigitalSignalTransfer(delegate)
			return new DigitalEqualOperation(leftSignal, right)
		}

		// OPERATEURS ANALOGIQUES POUR AnalogSensor
		AnalogSensor.metaClass.greaterThan = { rhs ->
			return new BiggerAnalogOperation(
					toAnalogSignal(delegate),
					toAnalogSignal(rhs)
			)
		}
		AnalogSensor.metaClass.greaterOrEqual = { rhs ->
			return new BiggerOrEqualAnalogOperation(
					toAnalogSignal(delegate),
					toAnalogSignal(rhs)
			)
		}
		AnalogSensor.metaClass.smallerThan = { rhs ->
			return new BiggerAnalogOperation(
					toAnalogSignal(rhs),
					toAnalogSignal(delegate)
			)
		}
		AnalogSensor.metaClass.smallerOrEqual = { rhs ->
			return new BiggerOrEqualAnalogOperation(
					toAnalogSignal(rhs),
					toAnalogSignal(delegate)
			)
		}
		AnalogSensor.metaClass.equalTo = { rhs ->
			return new EqualAnalogOperation(
					toAnalogSignal(delegate),
					toAnalogSignal(rhs)
			)
		}

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

	def analogSensor(String name) {
		[pin: { n -> ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createAnalogSensor(name, n) }]
	}

	def analogActuator(String name) {
		[pin: { n -> ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createAnalogActuator(name, n) }]
	}
	
	// state "name" means actuator becomes signal [and actuator becomes signal]*n
	def state(String name) {
		List<Action> actions = new ArrayList<Action>()
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions)
		// recursive closure to allow multiple and statements
		def closure
		closure = { actuator ->
			[becomes: { value ->
				Action action
				if (actuator instanceof AnalogActuator) {
					action = new AnalogAction()
					action.setActuator(actuator)
					action.setValue(new AnalogSignalConstant(value))
				} else {
					action = new DigitalAction()
					action.setActuator(actuator)
					action.setValue(value instanceof String ?
							(DigitalSignalConstant)((GroovuinoMLBinding)this.getBinding()).getVariable(value) :
							(DigitalSignalConstant)value)
				}
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

				[when: { expr ->

					// Si expr EST une Expression, alors c'est du AND/OR/equalTo etc.
					if (expr instanceof Expression) {
						def fromState = state1 instanceof String ? binding.getVariable(state1) : state1
						def toState   = state2 instanceof String ? binding.getVariable(state2) : state2

						((GroovuinoMLBinding) this.getBinding())
								.getGroovuinoMLModel()
								.createTransition(fromState, toState, expr)

						return
					}

					// Sinon, on tombe dans le mode "sensor becomes signal"
					return [
							becomes: { signal ->
								def sensorObj = expr instanceof String ?
										(DigitalSensor) binding.getVariable(expr) :
										(DigitalSensor) expr

								def signalObj = signal instanceof String ?
										(DigitalSignalConstant) binding.getVariable(signal) :
										(DigitalSignalConstant) signal

								def leftSignal = new DigitalSignalTransfer(sensorObj)
								def condition = new DigitalEqualOperation(leftSignal, signalObj)

								def fromState = state1 instanceof String ? binding.getVariable(state1) : state1
								def toState   = state2 instanceof String ? binding.getVariable(state2) : state2

								((GroovuinoMLBinding) this.getBinding())
										.getGroovuinoMLModel()
										.createTransition(fromState, toState, condition)
							}
					]
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
