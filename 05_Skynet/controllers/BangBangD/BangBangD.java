import java.util.Arrays;

/**
 * This robot follows the wall to the left. It tries to stay near the wall, but
 * if near a corner it decides to make a turn to the right and follow that wall.
 *
 * In case of a T-shaped wall, it makes a sharp turn to the left to stay at the
 * wall.
 */
public class BangBangD extends BaseRobot {
	private static final int PROXIMITY_CENTER_THRESHOLD = 100;
	private static final int PROXIMITY_SENSOR_THRESHOLD = 200;
	private static final int WALL_DISTANCE_THRESHOLD = 75;


	public static void main(String[] args) {
		BangBangD controller = new BangBangD();
		controller.run();
	}

	@Override
	protected void update() {
		double leftValue = getDistanceSensorDataRaw(Sensor.LEFT);
		double centerValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_L, Sensor.FRONT_R)).sum() / 2d;

		if (centerValue > PROXIMITY_CENTER_THRESHOLD) {
			driveRightMaxSpeed();
		} else if (leftValue < PROXIMITY_SENSOR_THRESHOLD) {
			driveLeftMaxSpeed();
		} else if (leftValue > WALL_DISTANCE_THRESHOLD) {
			driveRightMaxSpeed();
		} else {
			driveForwardMaxSpeed();
		}
	}
}