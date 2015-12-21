public class AbandonBallBehaviour extends Behaviour {
	private int state = 0; // 0 = no crash yet; 1 = not checked if ball or wall; 2 = it is indeed a wall or stuck ball
	
	private long startOfAbandoningAction = 0;
	private static final long ABANDONING_DURATION_BACKOFF = 200;	// [ms] time for backwards driving
	private static final long ABANDONING_DURATION_RETURN = 300;		// [ms] time for forwards driving
	private static final long ABANDONING_DURATION_TURN = 750;	// [ms]

	public AbandonBallBehaviour(BaseRobot baseRobot) {
		super(baseRobot);
	}
	
	@Override
	public boolean update() {
//		System.out.println("state: " + state + ", y: " + Math.abs(_baseRobot.getAccelerometerDataSmoothedAverage(Axis.Y)));
		
		if (_baseRobot.getTimeMillis() - startOfAbandoningAction < ABANDONING_DURATION_BACKOFF) {
			_baseRobot.driveBackwards();
			return true;
		}
		
		if (_baseRobot.getTimeMillis() - startOfAbandoningAction < ABANDONING_DURATION_BACKOFF + ABANDONING_DURATION_RETURN) {
			_baseRobot.driveForwardMaxSpeed();
			return true;
		}
		
		if (state == 2 && _baseRobot.getTimeMillis() - startOfAbandoningAction < ABANDONING_DURATION_BACKOFF + ABANDONING_DURATION_RETURN + ABANDONING_DURATION_TURN) {
			this._baseRobot.turnAroundLeft();
			return true;
		}
		
		double accelerometerY = Math.abs(_baseRobot.getAccelerometerDataSmoothedAverage(Axis.Y));
		if (accelerometerY > 2) {	// impact
			if (state == 0) {			// first one
				startOfAbandoningAction = _baseRobot.getTimeMillis();
				_baseRobot.driveBackwards();
				state = 1;
			} else if (state == 1) {	// second one -> it's a (ball stuck at a) wall
				state = 2;
			}
			
			return true;
		}
		
		// we didn't crash into something or we crashed into a ball that now moved away
		state = 0;
		
		return false;
	}
}
