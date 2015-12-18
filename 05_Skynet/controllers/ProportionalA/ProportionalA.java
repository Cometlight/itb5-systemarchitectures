/**
 * This robot runs INTO the light.
 */
public class ProportionalA extends BaseRobot {
	private double[][] k = { {0,0,0,0,1,1,1,1},
			 				 {1,1,1,1,0,0,0,0} };
	private double[] c = { 0d, 0d };

	public static void main(String[] args) {
		ProportionalA controller = new ProportionalA();
		controller.run();
	}

	@Override
	protected void update() {
		double[] s = getLightSensorDataSmoothed(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT, Sensor.BACK_RIGHT, Sensor.BACK_LEFT, Sensor.LEFT, Sensor.FRONT_LEFT, Sensor.FRONT_L);
			
		double[] a = Matrix.add( Matrix.multiply(k, s), c );
		
		// ensure that leftValue + rightValue == MAX_SPEED, not more, not less.
		double leftValue = MAX_SPEED / (a[0] + a[1]) * a[0];
		double rightValue = MAX_SPEED / (a[0] + a[1]) * a[1];
		
		setSpeed(leftValue, rightValue);
	}
}