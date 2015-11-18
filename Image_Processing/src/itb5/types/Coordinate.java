package itb5.types;

/**
 * Our implementation of the coordinate required by CalcCentroidsFilter.
 */
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
			return 1; 	// FIXME: not really needed but would be nice
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _x;
		result = prime * result + _y;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (_x != other._x)
			return false;
		if (_y != other._y)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "(" + this._x + "," + this._y + ")";
	}
}
