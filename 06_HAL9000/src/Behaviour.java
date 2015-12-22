/**
 * A specific behaviour, like for example identify when the robot is stuck.
 */
public abstract class Behaviour {
	protected BaseRobot _baseRobot;

    /**
     * Creates a new behavior object.
     * @param  baseRobot the robot this behavior is for
     */
	public Behaviour(BaseRobot baseRobot) {
		_baseRobot = baseRobot;
	}

    /**
     * Performs a step. Implementations should return true, if they want their
     * changes to speed be performed. Returning true results in a break in
     * execution, what means that no further behaviours will be called.
     */
	public abstract boolean update();
}
