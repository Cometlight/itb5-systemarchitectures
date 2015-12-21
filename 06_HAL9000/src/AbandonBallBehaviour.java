import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class AbandonBallBehaviour extends Behaviour {
	private long startOfAbandoningAction = 0;
	private static final long ABANDONING_DURATION_BACKOFF = 500;
	private static final long ABANDONING_DURATION_TURN = 800;	// [ms]
//	private static final long ABANDONING_DURATION_STABILIZE = 600;	// [ms]	// TODO: it's actually 1000 ...
//	private static final long ABANDONING_COOLDOWN = 1000;	// [ms]
	private static final long NUMBER_OF_SAVED_MEDIAN_VALUES = 100;
	private Map<Axis, Queue<Double>> lastMedianValues;

	public AbandonBallBehaviour(BaseRobot baseRobot) {
		super(baseRobot);
		lastMedianValues = new HashMap<>();
		lastMedianValues.put(Axis.X, new ArrayDeque<>());
		lastMedianValues.put(Axis.Y, new ArrayDeque<>());
	}

	@Override
	public boolean update() {
		lastMedianValues.get(Axis.X).add(_baseRobot.getAccelerometerDataSmoothed(Axis.X));
		lastMedianValues.get(Axis.Y).add(_baseRobot.getAccelerometerDataSmoothed(Axis.Y));
		if (lastMedianValues.values().size() > NUMBER_OF_SAVED_MEDIAN_VALUES) {
			lastMedianValues.get(Axis.X).poll();
			lastMedianValues.get(Axis.Y).poll();
		}
		
		double x = Math.abs(_baseRobot.getAccelerometerDataSmoothed(Axis.X));
		double y = Math.abs(_baseRobot.getAccelerometerDataSmoothed(Axis.Y));
		
		double oldestMedianX = lastMedianValues.get(Axis.X).peek();
		double oldestMedianY = lastMedianValues.get(Axis.Y).peek();
		double distanceValue = Arrays.stream(_baseRobot.getDistanceSensorDataSmoothed(Sensor.FRONT_L, Sensor.FRONT_R)).sum() / 2d;
		
		if (System.currentTimeMillis() - startOfAbandoningAction < ABANDONING_DURATION_BACKOFF) {
//			System.out.println("BACKOFF");
			_baseRobot.driveBackwards();
			return true;
		} else if (System.currentTimeMillis() - startOfAbandoningAction < ABANDONING_DURATION_TURN) {
			this._baseRobot.turnAroundLeft();
//			System.out.println("TURNAROUND");
			return true;
		} else if (distanceValue > 1000d && ( Math.abs(oldestMedianX - x) < 0.005 || Math.abs(oldestMedianY - y) < 0.005 ) ) {
			startOfAbandoningAction = System.currentTimeMillis();
//			System.out.println("STUCK: " + x + ", " + y );
//			System.out.println(Math.abs(oldestMedianX - x) + ", " + Math.abs(oldestMedianY - y));
			return true;
		}
		
		return false;
	}

}
