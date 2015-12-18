import java.util.Arrays;

public class BangBangA extends BaseRobot {
	private static final int DIFFERENCE_THRESHOLD = 300;

	public static void main(String[] args) {
		BangBangA controller = new BangBangA();
		controller.run();
	}

	@Override
	protected void update() {
		double leftValue = Arrays.stream(getLightSensorDataSmoothed(Sensor.FRONT_L, Sensor.FRONT_LEFT, Sensor.LEFT, Sensor.BACK_LEFT)).sum();
		double rightValue = Arrays.stream(getLightSensorDataSmoothed(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT, Sensor.BACK_RIGHT)).sum();
		
		if (Math.abs(leftValue-rightValue) <= DIFFERENCE_THRESHOLD) {
			driveForwardMaxSpeed();
		} else if (leftValue < rightValue) {
			driveLeftMaxSpeed();
		} else {
			driveRightMaxSpeed();
		}
	}
}