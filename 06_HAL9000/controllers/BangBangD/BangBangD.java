import java.util.Arrays;

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