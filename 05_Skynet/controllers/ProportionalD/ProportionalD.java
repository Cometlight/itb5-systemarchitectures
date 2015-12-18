/**
 * This robot follows the wall to the left. It can take turns to the right (in
 * corners) and sharp turns to the left (i.e. on T-shaped wall structures).
 *
 * In this controller, it is possible to achieve higher than recommended speeds
 * on one wheel during turns!
 */
public class ProportionalD extends BaseRobot {
	private	double[][] k = { {1, 0, 0, 0, 0, 0, 0.5, 1, 0},
							 {-1, 0, 0, 0, 0, 0, 0, 0, 2500}
	   					   };
	private double[] c = { 0, MAX_SPEED/6d };

	public static void main(String[] args) {
		ProportionalD controller = new ProportionalD();
		controller.run();
	}

	@Override
	protected void update() {
		double[] s = getDistanceSensorDataSmoothed(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT, Sensor.BACK_RIGHT, Sensor.BACK_LEFT, Sensor.LEFT, Sensor.FRONT_LEFT, Sensor.FRONT_L, Sensor.FRONT_L);

		s[s.length-1] = 1d / getDistanceSensorDataSmoothed(Sensor.FRONT_L);	// inversely proportional to support sharp curve to the left in case of 180deg turns

		double[] a = Matrix.add( Matrix.multiply(k, s), c );

		double leftValue = MAX_SPEED / (a[0] + a[1]) * a[0];
		double rightValue = MAX_SPEED / (a[0] + a[1]) * a[1];

		setSpeed(leftValue, rightValue);
	}
}