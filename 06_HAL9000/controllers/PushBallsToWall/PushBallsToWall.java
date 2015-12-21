import java.util.ArrayList;
import java.util.List;

public class PushBallsToWall extends BaseRobot {
	List<Behaviour> _behaviours;
	

//	@Override
//	protected void update() {
//		double leftValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_L, Sensor.FRONT_LEFT, Sensor.LEFT)).sum() / 3d;
//		double centerValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_L, Sensor.FRONT_R)).sum() / 2d;
//		double rightValue = Arrays.stream(getDistanceSensorDataRaw(Sensor.FRONT_R, Sensor.FRONT_RIGHT, Sensor.RIGHT)).sum() / 3d;
//		
//		System.out.println("leftValue: " + leftValue + "\n\tcenterValue: " + centerValue + "\n\trightValue: " + rightValue);
//		
//		if (leftValue > centerValue && leftValue > rightValue) {
//			driveLeftMaxSpeed();
//		} else if (centerValue > rightValue) {
//			driveForwardMaxSpeed();
//		} else {
//			driveRightMaxSpeed();
//		}
//	}
	
	public PushBallsToWall() {
		super();
		_behaviours = new ArrayList<>();
		_behaviours.add(new StuckTimoutBehaviour(this));
		_behaviours.add(new AbandonBallBehaviour(this));
//		_behaviours.add(new PushBallBehaviour(this));
		_behaviours.add(new FindBallBehaviour(this));
		
		
	}
	
	@Override
	protected void update() {
		for (Behaviour behaviour : _behaviours) {
			if (behaviour.update()) {
//				System.out.println(behaviour.getClass().getName());
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		PushBallsToWall controller = new PushBallsToWall();
		controller.run();
	}
}