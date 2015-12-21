public class PushBallBehaviour extends Behaviour {
	public PushBallBehaviour(BaseRobot baseRobot) {
		super(baseRobot);
	}

	@Override
	public boolean update() {
		double leftValue = _baseRobot.getDistanceSensorDataRaw(Sensor.FRONT_L);
		double rightValue = _baseRobot.getDistanceSensorDataRaw(Sensor.FRONT_R);
		
//		System.out.println("leftValue: " + leftValue + "\n\tcenterValue: " + centerValue + "\n\trightValue: " + rightValue);
		
		if (leftValue > rightValue && leftValue > 1500) {
			_baseRobot.driveLeftMaxSpeed();
			return true;
		} else if (rightValue > 1500) {
			_baseRobot.driveForwardMaxSpeed();
			return true;
		}
		
		return false;
	}
}
