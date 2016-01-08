import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.LightSensor;

/**
 * Initializes the robot to be used by more specialized robot controllers.
 *
 * Provides methods to access sensor values, also as a history of sensor values.
 */
public abstract class BaseRobot extends DifferentialWheels {
	private final int STEP_TIME = 8;
	protected final int MIN_SPEED = 0;
	protected final int MAX_SPEED = 1000;
	protected final int SENSOR_READING_INTERVALL = 8; // [ms]
	private final int NO_VALUES_FOR_SMOOTHING = 10;	// For all sensors except accelerometer
	private final int NO_VALUES_FOR_SMOOTHING_ACCELEROMETER = 5;

	private Map<Sensor, DistanceSensor> distanceSensors;
	private Map<Sensor, LightSensor> lightSensors;

	private Map<Sensor, Queue<Double>> distanceSensorValues;
	private Map<Sensor, Queue<Double>> lightSensorValues;
	private Map<Axis, Queue<Double>> accelerometerValues;

	/**
	 * Defines what the robot should do each step.
	 */
	protected abstract void update();

	public BaseRobot() {
		super();

		initDistanceSensors();
		initLightSensors();
		initAccelerometer();
		initDistanceSensorValues();
		initLightSensorValues();
		initAccelerometerValues();
	}

	private void initAccelerometerValues() {
		accelerometerValues = new HashMap<>();

		accelerometerValues.put(Axis.X, new ArrayDeque<Double>());
		accelerometerValues.put(Axis.Y, new ArrayDeque<Double>());
		accelerometerValues.put(Axis.Z, new ArrayDeque<Double>());
	}

	private void initAccelerometer() {
		getAccelerometer("accelerometer").enable(SENSOR_READING_INTERVALL);
	}

	private void initDistanceSensors() {
		distanceSensors = new HashMap<>();
		distanceSensors.put(Sensor.FRONT_R, getDistanceSensor("ps0"));
		distanceSensors.put(Sensor.FRONT_RIGHT, getDistanceSensor("ps1"));
		distanceSensors.put(Sensor.RIGHT, getDistanceSensor("ps2"));
		distanceSensors.put(Sensor.BACK_RIGHT, getDistanceSensor("ps3"));
		distanceSensors.put(Sensor.BACK_LEFT, getDistanceSensor("ps4"));
		distanceSensors.put(Sensor.LEFT, getDistanceSensor("ps5"));
		distanceSensors.put(Sensor.FRONT_LEFT, getDistanceSensor("ps6"));
		distanceSensors.put(Sensor.FRONT_L, getDistanceSensor("ps7"));

		distanceSensors.entrySet().forEach(proximitySensor -> proximitySensor.getValue().enable(SENSOR_READING_INTERVALL));
	}

	private void initLightSensors() {
		lightSensors = new HashMap<>();
		lightSensors.put(Sensor.FRONT_R, getLightSensor("ls0"));
		lightSensors.put(Sensor.FRONT_RIGHT, getLightSensor("ls1"));
		lightSensors.put(Sensor.RIGHT, getLightSensor("ls2"));
		lightSensors.put(Sensor.BACK_RIGHT, getLightSensor("ls3"));
		lightSensors.put(Sensor.BACK_LEFT, getLightSensor("ls4"));
		lightSensors.put(Sensor.LEFT, getLightSensor("ls5"));
		lightSensors.put(Sensor.FRONT_LEFT, getLightSensor("ls6"));
		lightSensors.put(Sensor.FRONT_L, getLightSensor("ls7"));

		lightSensors.entrySet().forEach(lightSensor -> lightSensor.getValue().enable(SENSOR_READING_INTERVALL));
	}

	private void initDistanceSensorValues() {
		distanceSensorValues = new HashMap<>();
		distanceSensorValues.put(Sensor.FRONT_R, new ArrayDeque<Double>());
		distanceSensorValues.put(Sensor.FRONT_RIGHT, new ArrayDeque<Double>());
		distanceSensorValues.put(Sensor.RIGHT, new ArrayDeque<Double>());
		distanceSensorValues.put(Sensor.BACK_RIGHT, new ArrayDeque<Double>());
		distanceSensorValues.put(Sensor.BACK_LEFT, new ArrayDeque<Double>());
		distanceSensorValues.put(Sensor.LEFT, new ArrayDeque<Double>());
		distanceSensorValues.put(Sensor.FRONT_LEFT, new ArrayDeque<Double>());
		distanceSensorValues.put(Sensor.FRONT_L, new ArrayDeque<Double>());
	}

	private void initLightSensorValues() {
		lightSensorValues = new HashMap<>();
		lightSensorValues.put(Sensor.FRONT_R, new ArrayDeque<Double>());
		lightSensorValues.put(Sensor.FRONT_RIGHT, new ArrayDeque<Double>());
		lightSensorValues.put(Sensor.RIGHT, new ArrayDeque<Double>());
		lightSensorValues.put(Sensor.BACK_RIGHT, new ArrayDeque<Double>());
		lightSensorValues.put(Sensor.BACK_LEFT, new ArrayDeque<Double>());
		lightSensorValues.put(Sensor.LEFT, new ArrayDeque<Double>());
		lightSensorValues.put(Sensor.FRONT_LEFT, new ArrayDeque<Double>());
		lightSensorValues.put(Sensor.FRONT_L, new ArrayDeque<Double>());
	}

	/**
	 * Should be called once in main().
	 */
	public void run() {
		while (step(STEP_TIME) != -1) {
			updateSensorValues();
			update(); // defined in implementing class
		}
	}

	private void updateSensorValues() {
		if (distanceSensorValues.values().size() > 0) {
			int nrOfValuesDistanceSensor = distanceSensorValues.values().iterator().next().size();
			if (nrOfValuesDistanceSensor >= NO_VALUES_FOR_SMOOTHING) {
				deleteOldestValue();
			}
			int nrOfValuesAccelerometerSensor = accelerometerValues.values().iterator().next().size();
			if (nrOfValuesAccelerometerSensor >= NO_VALUES_FOR_SMOOTHING_ACCELEROMETER) {
				deleteOldestAccelerometerValue();
			}
			insertNewValue();
		}
	}

	private void deleteOldestAccelerometerValue() {
		accelerometerValues.values().forEach(queue -> queue.poll());
	}

	private void deleteOldestValue() {
		distanceSensorValues.values().forEach(queue -> queue.poll());
		lightSensorValues.values().forEach(queue -> queue.poll());
	}

	private void insertNewValue() {
		distanceSensorValues.entrySet()
				.forEach(entry -> entry.getValue().add(distanceSensors.get(entry.getKey()).getValue()));
		lightSensorValues.entrySet()
				.forEach(entry -> entry.getValue().add(lightSensors.get(entry.getKey()).getValue()));

		accelerometerValues.entrySet().forEach(
				entry -> entry.getValue().add( Math.abs(getAccelerometerValue(entry.getKey())) ));

	}

	private double getAccelerometerValue(Axis axis) {
		double[] values = getAccelerometer("accelerometer").getValues();
		switch(axis) {
		case X:
			return values[0];
		case Y:
			return values[1];
		case Z:
			return values[2];
		default:
			return 0d;
		}

	}

	public double getDistanceSensorDataRaw(Sensor sensor) {
		return distanceSensors.get(sensor).getValue();
	}

	public double[] getDistanceSensorDataRaw(Sensor... sensors) {
		double[] values = new double[sensors.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = getDistanceSensorDataRaw(sensors[i]);
		}
		return values;
	}

	public double getLightSensorDataRaw(Sensor sensor) {
		return lightSensors.get(sensor).getValue();
	}

	public double[] getLightSensorDataRaw(Sensor... sensors) {
		double[] values = new double[sensors.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = getLightSensorDataRaw(sensors[i]);
		}
		return values;
	}

	public double getDistanceSensorDataSmoothed(Sensor sensor) {
		return getMedianValue(distanceSensorValues.get(sensor));
	}

	public double[] getDistanceSensorDataSmoothed(Sensor... sensors) {
		double[] values = new double[sensors.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = getDistanceSensorDataSmoothed(sensors[i]);
		}
		return values;
	}

	public double getLightSensorDataSmoothed(Sensor sensor) {
		return getMedianValue(lightSensorValues.get(sensor));
	}

	public double[] getLightSensorDataSmoothed(Sensor... sensors) {
		double[] values = new double[sensors.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = getLightSensorDataSmoothed(sensors[i]);
		}
		return values;
	}

	public double getAccelerometerDataSmoothedMedian(Axis axis) {
		return getMedianValue(accelerometerValues.get(axis));
	}

	public double getAccelerometerDataSmoothedAverage(Axis axis) {
		return getAverageValue(accelerometerValues.get(axis));
	}

	private double getMedianValue(Queue<Double> queue) {
		Double[] sorted = queue.toArray(new Double[0]);
		Arrays.sort(sorted);

		if (sorted.length == 1) {
			return sorted[0];
		} else if (sorted.length % 2 == 1) {
			return (sorted[sorted.length / 2 - 1] + sorted[sorted.length / 2]) / 2d;
		} else {
			return sorted[sorted.length / 2];
		}
	}

	private double getAverageValue(Queue<Double> queue) {
		return queue.stream().mapToDouble(d -> d).average().orElse(0d);
	}

	protected void driveLeftMaxSpeed() {
		setSpeed(MIN_SPEED, MAX_SPEED);
	}

	protected void driveRightMaxSpeed() {
		setSpeed(MAX_SPEED, MIN_SPEED);
	}

	protected void driveForwardMaxSpeed() {
		setSpeed(MAX_SPEED, MAX_SPEED);
	}

	protected void driveStop() {
		setSpeed(MIN_SPEED, MIN_SPEED);
	}

	protected void turnAroundLeft() {
		setSpeed(MAX_SPEED, -MAX_SPEED);
	}

	protected void turnAroundRight() {
		setSpeed(-MAX_SPEED, MAX_SPEED);
	}

	protected void driveBackwards() {
		setSpeed(-MAX_SPEED, -MAX_SPEED);
	}

	@Override
	public void setSpeed(double left, double right) {
		right = right == 0 ? 1 : right;
		left = left == 0 ? 1 : left;

		double factor = 1;

		if (right > left) {
			factor = 1000d/Math.abs(right);
		} else {
			factor = 1000d/Math.abs(left);
		}

		super.setSpeed(left * factor, right * factor);
	}

	public long getTimeMillis() {
		return (long) (getTime() * 1000);
	}
}
