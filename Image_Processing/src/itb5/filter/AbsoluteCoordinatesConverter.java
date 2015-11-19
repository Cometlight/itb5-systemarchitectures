package itb5.filter;

import java.security.InvalidParameterException;
import java.util.LinkedList;

import itb5.types.Coordinate;
import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

/**
 * Transforms relative coordinates (in the Region of Interest) to global
 * coordinates (in the whole image).
 */
public class AbsoluteCoordinatesConverter extends DataTransformationFilter<LinkedList<Coordinate>> {

	private Coordinate _upperLeft;
	
	public AbsoluteCoordinatesConverter(Coordinate upperLeft, Readable<LinkedList<Coordinate>> input, Writeable<LinkedList<Coordinate>> output)
			throws InvalidParameterException {
		super(input, output);
		_upperLeft = upperLeft;
	}
	
	public AbsoluteCoordinatesConverter(Coordinate upperLeft, Readable<LinkedList<Coordinate>> input)
			throws InvalidParameterException {
		super(input);
		_upperLeft = upperLeft;
	}
	
	public AbsoluteCoordinatesConverter(Coordinate upperLeft, Writeable<LinkedList<Coordinate>> output)
			throws InvalidParameterException {
		super(output);
		_upperLeft = upperLeft;
	}

	@Override
	protected void process(LinkedList<Coordinate> entity) {
		for(Coordinate coordiante : entity) {
			coordiante._x += _upperLeft._x;
			coordiante._y += _upperLeft._y;
		}
	}

}
