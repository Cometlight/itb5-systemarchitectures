package itb5.types;

public class Coordinate implements Comparable<Coordinate> {
	public int _x;
	public int _y;
	
	public Coordinate(int x, int y) {
		_x = x;
		_y = y;
	}

	@Override
	public int compareTo(Coordinate o) {
		if(o._x == this._x && o._y == this._y) {
			return 0;
		} else {
			return 1; 	// FIXME
		}
	}
}
