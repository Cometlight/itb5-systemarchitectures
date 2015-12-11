import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.LightSensor;

public class ProportionalA extends DifferentialWheels {
	private static final int STEP_TIME = 8;
	private static final int MIN_SPEED = 0;
	private static final int MAX_SPEED = 1000;
	private static final int PROXIMITY_SENSOR_L = 0; // sensor left
	private static final int PROXIMITY_SENSOR_LF = 1; // sensor left front
	private static final int PROXIMITY_SENSOR_RF = 2; // sensor right front
	private static final int PROXIMITY_SENSOR_R = 3; // sensor right
	private static final int PROXIMITY_SENSOR_MAX = 200; // maximum distance for sensors
	
	private static final int LIGHT_SENSOR_THRESHOLD = 3500;

	private DistanceSensor[] proximitySensors;
	private Map<String /* name */, LightSensor> lightSensors;

	public ProportionalA() {
		super();

		proximitySensors = new DistanceSensor[] { 
				getDistanceSensor("ps5"), 
				getDistanceSensor("ps7"),
				getDistanceSensor("ps0"), 
				getDistanceSensor("ps2") 
		};
		
		// enable all proximity sensors
		for (int i = 0; i < proximitySensors.length; i++) {
			proximitySensors[i].enable(10);
		}
		
		lightSensors = new HashMap<>();
		lightSensors.put("front_r", getLightSensor("ls0"));
		lightSensors.put("front_right", getLightSensor("ls1"));
		lightSensors.put("right", getLightSensor("ls2"));
		lightSensors.put("back_right", getLightSensor("ls3"));
		lightSensors.put("back_left", getLightSensor("ls4"));
		lightSensors.put("left", getLightSensor("ls5"));
		lightSensors.put("front_l", getLightSensor("ls7"));
		lightSensors.put("front_left", getLightSensor("ls6"));
		
		
		
		lightSensors.entrySet().forEach(lightSensor -> lightSensor.getValue().enable(10));
		

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
			
			
			double[][] k = { {0,0,0,0,1,1,1,1},
							 {1,1,1,1,0,0,0,0} };
			
			double[] s = {  lightSensors.get("front_r").getValue(),
							lightSensors.get("front_right").getValue(),
							lightSensors.get("right").getValue(),
							lightSensors.get("back_right").getValue(),
							lightSensors.get("back_left").getValue(),
							lightSensors.get("left").getValue(),
							lightSensors.get("front_left").getValue(),
							lightSensors.get("front_l").getValue()
						 };
			
			double[] c = { 0d, 0d };
				
			// TODO: delete and improve
//			double leftValue = lightSensors.get("front_l").getValue() + lightSensors.get("front_left").getValue() + lightSensors.get("left").getValue() + lightSensors.get("back_left").getValue();
//			double rightValue = lightSensors.get("front_r").getValue() + lightSensors.get("front_right").getValue() + lightSensors.get("right").getValue() + lightSensors.get("back_right").getValue();
			
			double[] a = Matrix.add( Matrix.multiply(k, s), c );
			
			double leftValue = 1000d / (a[0] + a[1]) * a[0];
			double rightValue = 1000d / (a[0] + a[1]) * a[1];
			
			setSpeed(leftValue, rightValue);
			
			
			

			
//			if (leftValue < rightValue) {
//				driveLeft();
//			} else {
//				driveRight();
//			}
			
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
		ProportionalA controller = new ProportionalA();
		controller.run();
	}

	public void driveLeft() {
		setSpeed(MIN_SPEED, MAX_SPEED);
	}

	public void driveRight() {
		setSpeed(MAX_SPEED, MIN_SPEED);
	}

	public void driveForward() {
		setSpeed(MAX_SPEED, MAX_SPEED);
	}
}