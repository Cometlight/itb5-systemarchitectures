public class FindBallBehaviour extends Behaviour {
	private double[][] k = { {1, 0.9, 0.6,   0,   0, 0,     0, 0},
							 {0,   0,   0,   0,   0, 0.6, 0.9, 1}
			   			   };
	private double[] c = { 0, 0d };

	public FindBallBehaviour(BaseRobot baseRobot) {
		super(baseRobot);
	}

	@Override
	public boolean update() {
		double[] s = _baseRobot.getDistanceSensorDataSmoothed(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT, Sensor.BACK_RIGHT, Sensor.BACK_LEFT, Sensor.LEFT, Sensor.FRONT_LEFT, Sensor.FRONT_L);
		
		double[] a = Matrix.add( Matrix.multiply(k, s), c );
		
		double leftValue = _baseRobot.MAX_SPEED / (a[0] + a[1]) * a[0];
		double rightValue = _baseRobot.MAX_SPEED / (a[0] + a[1]) * a[1];
		
		_baseRobot.setSpeed(leftValue, rightValue);
		
		return true;
	}

}
