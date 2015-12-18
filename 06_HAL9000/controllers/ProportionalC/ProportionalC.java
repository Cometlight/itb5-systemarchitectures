public class ProportionalC extends BaseRobot {
	private double[][] k = { {0.65, 0.25, 0.1, 0,   0, 0, 0,    0},
							 {0,    0,    0,   0.1, 0, 0, 0.25, 0.65}
	   					   };
	private double[] c = { 0, 0d };
	
	public static void main(String[] args) {
		ProportionalC controller = new ProportionalC();
		controller.run();
	}

	@Override
	protected void update() {
		double[] s = getDistanceSensorDataSmoothed(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT, Sensor.BACK_RIGHT, Sensor.BACK_LEFT, Sensor.LEFT, Sensor.FRONT_LEFT, Sensor.FRONT_L);
		
		double[] a = Matrix.add( Matrix.multiply(k, s), c );
		
		double leftValue = MAX_SPEED / (a[0] + a[1]) * a[0];
		double rightValue = MAX_SPEED / (a[0] + a[1]) * a[1];
		
		setSpeed(leftValue, rightValue);
	}
}