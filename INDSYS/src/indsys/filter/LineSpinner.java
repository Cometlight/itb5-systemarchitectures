package indsys.filter;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.LinkedList;

import indsys.types.Line;
import pimpmypipe.filter.AbstractFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class LineSpinner extends AbstractFilter<Line, Line> {
	private LinkedList<Line> _lines;

	public LineSpinner(Readable<Line> input, Writeable<Line> output) throws InvalidParameterException {
		super(input, output);
		_lines = new LinkedList<>();
	}

	@Override
	public Line read() throws StreamCorruptedException {
		if(_lines.isEmpty()) {
			processLine(this.readInput());
		}
		
		if(_lines.isEmpty()) {
			return null;
		} else {
			return _lines.pop();
		}
	}

	private void processLine(Line line) {
		if(line != null) {
			int nrOfWords = line.getWords().size();
			for(int i = 0; i < nrOfWords; ++i) {
				Line newLine = new Line(line.getLineNumber());
				for(int j = 0; j < nrOfWords; ++j) {
					newLine.addWord(line.getWords().get( (i+j) % nrOfWords));
				}
				_lines.add(newLine);
			}
		}
		
	}

	@Override
	public void write(Line value) throws StreamCorruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}


}
