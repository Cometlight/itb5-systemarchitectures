package itb5.filter;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.LinkedList;

import itb5.types.Coordinate;
import pimpmypipe.filter.AbstractFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class ToleranceChecker extends AbstractFilter<LinkedList<Coordinate>, LinkedList<Boolean>> {
	LinkedList<Coordinate> _expected;
	int _tolerance;

	public ToleranceChecker(LinkedList<Coordinate> expected, int tolerance, Readable<LinkedList<Coordinate>> input, Writeable<LinkedList<Boolean>> output)
			throws InvalidParameterException {
		super(input, output);
		init(expected, tolerance);
	}
	
	public ToleranceChecker(LinkedList<Coordinate> expected, int tolerance, Readable<LinkedList<Coordinate>> input)
			throws InvalidParameterException {
		super(input);
		init(expected, tolerance);
	}
	
	public ToleranceChecker(LinkedList<Coordinate> expected, int tolerance, Writeable<LinkedList<Boolean>> output)
			throws InvalidParameterException {
		super(output);
		init(expected, tolerance);
	}

	private void init(LinkedList<Coordinate> expected, int tolerance) {
		_expected = expected;
		_tolerance = tolerance;
	}
	
	@Override
	public LinkedList<Boolean> read() throws StreamCorruptedException {
		LinkedList<Boolean> results = analyze(this.readInput());
		return results;
	}

	@Override
	public void write(LinkedList<Coordinate> coordinates) throws StreamCorruptedException {
		LinkedList<Boolean> results = analyze(coordinates);
		this.writeOutput(results);
	}
	
	private LinkedList<Boolean> analyze(LinkedList<Coordinate> coordinates) {
		LinkedList<Boolean> results = new LinkedList<>();
		
		if(coordinates == null || coordinates.size() != _expected.size()) {
			return null;
		}
		
		Iterator<Coordinate> it1 = coordinates.iterator();
		Iterator<Coordinate> it2 = _expected.iterator();
		
		while(it1.hasNext()) {
			Coordinate c1 = it1.next();
			Coordinate c2 = it2.next();
			results.add(Math.abs(c1._x - c2._x) <= _tolerance && Math.abs(c1._y - c2._y) <= _tolerance);
		}
		
		return results;
	}

	@Override
	public void run() {
		LinkedList<Boolean> output = null;//getNewEntityObject();
        try {
            do {
                output = read();

                if (output != null) {
                    writeOutput(output);
                }
            }while(output != null);
            sendEndSignal();
        } catch (StreamCorruptedException e) {
            System.out.print("Thread reports error: ");
            System.out.println(Thread.currentThread().getId() + " (" + Thread.currentThread().getName() + ")");
            e.printStackTrace();
        }
	}

}
