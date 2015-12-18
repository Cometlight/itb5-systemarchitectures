import java.util.Arrays;

/**
 * This robot pushes a ball and does not do much else.
 * First it searches for an object and runs toward it. The robot can distinguish
 * three cases:
 *   1) object to the front-left    -> go to the front-left
 *   2) object to the front         -> go forward
 *   3) object to the front-right   -> go to the front-right
 */
public class BangBangC extends BaseRobot {

	@Override
	protected void update() {
		double leftValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_L, Sensor.FRONT_LEFT, Sensor.LEFT)).sum() / 3d;
		double centerValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_L, Sensor.FRONT_R)).sum() / 2d;
		double rightValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT)).sum() / 3d;

		if (leftValue > centerValue && leftValue > rightValue) {
			driveLeftMaxSpeed();
		} else if (centerValue > rightValue) {
			driveForwardMaxSpeed();
		} else {
			driveRightMaxSpeed();
		}
	}

	public static void main(String[] args) {
		BangBangC controller = new BangBangC();
		controller.run();
	}
}