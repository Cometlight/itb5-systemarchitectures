import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.LightSensor;

public class BangBangC extends DifferentialWheels {
	private static final int STEP_TIME = 8;
	private static final int MIN_SPEED = 0;
	private static final int MAX_SPEED = 1000;
	private static final int PROXIMITY_SENSOR_L = 0; // sensor left
	private static final int PROXIMITY_SENSOR_LF = 1; // sensor left front
	private static final int PROXIMITY_SENSOR_RF = 2; // sensor right front
	private static final int PROXIMITY_SENSOR_R = 3; // sensor right
	private static final int PROXIMITY_SENSOR_MAX = 200; // maximum distance for sensors
	
	private static final int PROXIMITY_SENSOR_THRESHOLD = 80;
	private static final int SENSOR_READING_INTERVALL = 10;	// [ms]

	private Map<String /* name */, DistanceSensor> proximitySensors;
	private Map<String /* name */, LightSensor> lightSensors;

	public BangBangC() {
		super();

		proximitySensors = new HashMap<>();
		proximitySensors.put("front_r", getDistanceSensor("ps0"));
		proximitySensors.put("front_right", getDistanceSensor("ps1"));
		proximitySensors.put("right", getDistanceSensor("ps2"));
		proximitySensors.put("back_right", getDistanceSensor("ps3"));
		proximitySensors.put("back_left", getDistanceSensor("ps4"));
		proximitySensors.put("left", getDistanceSensor("ps5"));
		proximitySensors.put("front_l", getDistanceSensor("ps7"));
		proximitySensors.put("front_left", getDistanceSensor("ps6"));
		
		proximitySensors.entrySet().forEach(proximitySensor -> proximitySensor.getValue().enable(SENSOR_READING_INTERVALL));
		
		// You should insert a getDevice-like function in order to get the
		// instance of a device of the robot. Something like:
		// led = getLED("ledName");

	}

	// User defined function for initializing and running
	// the BangBangController class
	public void run() {

		// Main loop:
		// Perform simulation steps of 64 milliseconds
		// and leave the loop when the simulation is over
		int myStep = 0;
		while (step(STEP_TIME) != -1) {
			// Read the sensors:
			// Enter here functions to read sensor data, like:
			// double val = distanceSensor.getValue();

			// Process sensor data here
			
			//double l = proximitySensors[PROXIMITY_SENSOR_L].getValue();
			//double r = proximitySensors[PROXIMITY_SENSOR_R].getValue();
			
			//setSpeed(MAX_SPEED * r / l, MAX_SPEED * l / r);

//			Optional<Entry<String, LightSensor>> maxLightSensorOptional = lightSensors.entrySet().stream().max((l1, l2) -> (int)(l1.getValue().getValue() - l2.getValue().getValue()));
//			LightSensor maxLightSensor = maxLightSensorOptional.get().getValue();
//			System.out.println(maxLightSensor.getName());
			
			double leftValue = (proximitySensors.get("left").getValue() + proximitySensors.get("front_left").getValue() + proximitySensors.get("front_l").getValue()) / 3d;
			double centerValue = (proximitySensors.get("front_l").getValue() + proximitySensors.get("front_r").getValue()) / 2d;
			double rightValue = (proximitySensors.get("right").getValue() + proximitySensors.get("front_right").getValue() + proximitySensors.get("front_r").getValue()) / 3d;
			
			if (leftValue > centerValue && leftValue > rightValue) {
				driveLeft();
			} else if (centerValue > rightValue) {
				driveForward();
			} else {
				driveRight();
			}
			
//			if (proximitySensors[PROXIMITY_SENSOR_L].getValue() > PROXIMITY_SENSOR_MAX
//					|| proximitySensors[PROXIMITY_SENSOR_LF].getValue() > PROXIMITY_SENSOR_MAX) {
//				driveRight();
//			} else if (proximitySensors[PROXIMITY_SENSOR_R].getValue() > PROXIMITY_SENSOR_MAX
//					|| proximitySensors[PROXIMITY_SENSOR_RF].getValue() > PROXIMITY_SENSOR_MAX) {
//				driveLeft();
//			} else {
//				driveForward();
//			}

			// Enter here functions to send actuator commands, like:
			// led.set(1);
		}
		// Enter here exit cleanup code
	}


	public static void main(String[] args) {
		BangBangC controller = new BangBangC();
		controller.run();
	}

	public void driveLeft() {
		System.out.println(" Left ");
		setSpeed(MIN_SPEED, MAX_SPEED);
	}

	public void driveRight() {
		System.out.println(" Right ");
		setSpeed(MAX_SPEED, MIN_SPEED);
	}

	public void driveForward() {
		setSpeed(MAX_SPEED, MAX_SPEED);
	}
	
	public void stop() {
		System.out.println("### STOP ###");
		setSpeed(0.1, 0.1);
	}
}