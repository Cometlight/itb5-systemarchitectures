/**
 * Uses the distance sensors to find and drive into balls, pushing them in doing
 * so.
 */
public class FindBallBehaviour extends Behaviour {
	private double[][] k = { {1, 0.9, 0.6,   0,   0, 0,     0, 0},	// We emphasize the sensor values at the front.
							 {0,   0,   0,   0,   0, 0.6, 0.9, 1}
			   			   };
	private double[] c = { 0, 0d };

	public FindBallBehaviour(BaseRobot baseRobot) {
		super(baseRobot);
	}

	@Override
	public boolean update() {
		double[] s = _baseRobot.getDistanceSensorDataSmoothed(
            Sensor.FRONT_R,
            Sensor.FRONT_RIGHT,
            Sensor.RIGHT,
            Sensor.BACK_RIGHT,
            Sensor.BACK_LEFT,
            Sensor.LEFT,
            Sensor.FRONT_LEFT,
            Sensor.FRONT_L
        );

        // proportional
		double[] a = Matrix.add(Matrix.multiply(k, s), c);

		_baseRobot.setSpeed(a[0], a[1]);

		return true;
	}

}
