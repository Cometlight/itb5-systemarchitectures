import java.util.Arrays;

public class BangBangB extends BaseRobot {
	private static final int LIGHT_SENSOR_LOWERBOUND = 3000;

	@Override
	protected void update() {
		double leftValue = Arrays.stream(getLightSensorDataSmoothed(Sensor.FRONT_L, Sensor.FRONT_LEFT, Sensor.LEFT, Sensor.BACK_LEFT)).sum() / 4d;
		double rightValue = Arrays.stream(getLightSensorDataSmoothed(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT, Sensor.BACK_RIGHT)).sum() / 4d;
		double frontValue = Arrays.stream(getLightSensorDataSmoothed(Sensor.FRONT_L, Sensor.FRONT_R)).sum() / 2d;
		
		if (frontValue < LIGHT_SENSOR_LOWERBOUND) {
			driveStop();
		} else if (leftValue < rightValue) {
			driveLeftMaxSpeed();
		} else {
			driveRightMaxSpeed();
		}
	}

	public static void main(String[] args) {
		BangBangB controller = new BangBangB();
		controller.run();
	}
}