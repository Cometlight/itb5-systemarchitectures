import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;

public class BangBangA extends DifferentialWheels {
	private static final int STEP_TIME = 8;
	private static final int MIN_SPEED = 0;
	private static final int MAX_SPEED = 1000;
	private static final int PROXIMITY_SENSOR_L = 0; // sensor left
	private static final int PROXIMITY_SENSOR_LF = 1; // sensor left front
	private static final int PROXIMITY_SENSOR_RF = 2; // sensor right front
	private static final int PROXIMITY_SENSOR_R = 3; // sensor right
	private static final int PROXIMITY_SENSOR_MAX = 200; // maximum distance for sensors

	private DistanceSensor[] proximitySensors;

	public BangBangA() {
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

			if (proximitySensors[PROXIMITY_SENSOR_L].getValue() > PROXIMITY_SENSOR_MAX
					|| proximitySensors[PROXIMITY_SENSOR_LF].getValue() > PROXIMITY_SENSOR_MAX) {
				driveRight();
			} else if (proximitySensors[PROXIMITY_SENSOR_R].getValue() > PROXIMITY_SENSOR_MAX
					|| proximitySensors[PROXIMITY_SENSOR_RF].getValue() > PROXIMITY_SENSOR_MAX) {
				driveLeft();
			} else {
				driveForward();
			}

			// Enter here functions to send actuator commands, like:
			// led.set(1);
		}

		// Enter here exit cleanup code
	}


	public static void main(String[] args) {
		BangBangA controller = new BangBangA();
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