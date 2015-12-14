public class ProportionalB extends BaseRobot {
	private static final int LIGHT_SENSOR_LOWERBOUND = 3100;
	private static final int LIGHT_SENSOR_UPPERBOUND = 4000;


	public static void main(String[] args) {
		ProportionalB controller = new ProportionalB();
		controller.run();
	}

	public void driveLeft() {
		System.out.println(" Left ");
		setSpeed(MIN_SPEED, MAX_SPEED);
	}

	public void driveRight() {
		System.out.println(" Right ");
		setSpeed(MAX_SPEED, MIN_SPEED);
	}

	public void driveForward() {
		setSpeed(MAX_SPEED, MAX_SPEED);
	}
	
	public void stop() {
		System.out.println("### STOP ###");
		setSpeed(0.1, 0.1);
	}


	@Override
	protected void update() {
		double[][] k = { {0,0,0,0.1,0.25,0.65},
						 {0.65,0.25,0.1,0,0,0} };

		double[] s = getLightSensorDataSmoothed(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT, Sensor.LEFT, Sensor.FRONT_LEFT, Sensor.FRONT_L);
		
		double[] c = { 0, 0d };
		
		double[] a = Matrix.add( Matrix.multiply(k, s), c );
		
		double lightSensorRange = LIGHT_SENSOR_UPPERBOUND - LIGHT_SENSOR_LOWERBOUND;
		a[0] = Math.max(0,a[0]-LIGHT_SENSOR_LOWERBOUND) / lightSensorRange * MAX_SPEED;
		a[1] = Math.max(0,a[1]-LIGHT_SENSOR_LOWERBOUND) / lightSensorRange * MAX_SPEED;
		
		a[0] = Math.min(MAX_SPEED, a[0]);
		a[1] = Math.min(MAX_SPEED, a[1]);
				
		
		setSpeed(a[0], a[1]);
	}
}