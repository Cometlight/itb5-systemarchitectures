import java.util.Arrays;

public class BangBangC extends BaseRobot {

	@Override
	protected void update() {
		double leftValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_L, Sensor.FRONT_LEFT, Sensor.LEFT)).sum() / 3d;
		double centerValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_L, Sensor.FRONT_R)).sum() / 2d;
		double rightValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT)).sum() / 3d;
		
		System.out.println("leftValue: " + leftValue + "\n\tcenterValue: " + centerValue + "\n\trightValue: " + rightValue);
		
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