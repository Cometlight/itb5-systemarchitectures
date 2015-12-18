/**
 *  This robot runs TO the light and stops near it slowing down in its proximity.
 */
public class ProportionalB extends BaseRobot {
	// if value is smaller, then robot stops
	private static final int LIGHT_SENSOR_LOWERBOUND = 3100;
	
	// if value is bigger, then robot drives with full speed
	private static final int LIGHT_SENSOR_UPPERBOUND = 4000;

	private double[][] k = { {0,    0,    0,   0.1, 0, 0, 0.25, 0.65},
			 				 {0.65, 0.25, 0.1, 0,   0, 0, 0,    0} };
	private double[] c = { 0, 0d };

	public static void main(String[] args) {
		ProportionalB controller = new ProportionalB();
		controller.run();
	}

	@Override
	protected void update() {
		double[] s = getLightSensorDataSmoothed(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT, Sensor.BACK_RIGHT, Sensor.BACK_LEFT, Sensor.LEFT, Sensor.FRONT_LEFT, Sensor.FRONT_L);
		
		double[] a = Matrix.add( Matrix.multiply(k, s), c );
		
		// Usage of Math.max and Math.min ensures that the speed set is between 0 and MAX_SPEED.
		double lightSensorRange = LIGHT_SENSOR_UPPERBOUND - LIGHT_SENSOR_LOWERBOUND;
		a[0] = Math.max(0,a[0]-LIGHT_SENSOR_LOWERBOUND) / lightSensorRange * MAX_SPEED;
		a[1] = Math.max(0,a[1]-LIGHT_SENSOR_LOWERBOUND) / lightSensorRange * MAX_SPEED;
		
		a[0] = Math.min(MAX_SPEED, a[0]);
		a[1] = Math.min(MAX_SPEED, a[1]);
		
		setSpeed(a[0], a[1]);
	}
}