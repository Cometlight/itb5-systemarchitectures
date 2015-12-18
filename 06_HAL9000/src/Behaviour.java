
public abstract class Behaviour {
	protected BaseRobot _baseRobot;
	
	public Behaviour(BaseRobot baseRobot) {
		_baseRobot = baseRobot;
	}
	
	public abstract boolean update();
}
