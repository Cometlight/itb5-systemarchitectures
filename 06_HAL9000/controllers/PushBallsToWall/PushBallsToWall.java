import java.util.ArrayList;
import java.util.List;

/**
 * Finds balls and pushes them to the wall. 
 */
public class PushBallsToWall extends BaseRobot {
	List<Behaviour> _behaviours;	// the behaviours at the front are given precedence.
	
	public PushBallsToWall() {
		super();
		_behaviours = new ArrayList<>();
		_behaviours.add(new StuckTimoutBehaviour(this));
		_behaviours.add(new AbandonObjectBehaviour(this));
		_behaviours.add(new FindBallBehaviour(this));
	}
	
	@Override
	protected void update() {
		for (Behaviour behaviour : _behaviours) {
			if (behaviour.update()) {
//				Print out current behaviour:
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