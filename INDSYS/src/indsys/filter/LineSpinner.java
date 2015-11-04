package indsys.filter;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import indsys.types.Line;
import pimpmypipe.filter.AbstractFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class LineSpinner extends AbstractFilter<Line, Line> {
	private static final Logger _log = Logger.getLogger(LineSpinner.class.getName());
	
	private LinkedList<Line> _lines = new LinkedList<>();

	public LineSpinner(Readable<Line> input, Writeable<Line> output) throws InvalidParameterException {
		super(input, output);
	}
	
	public LineSpinner(Readable<Line> input) throws InvalidParameterException {
		super(input);
	}
	
	public LineSpinner(Writeable<Line> output) throws InvalidParameterException {
		super(output);
	}

	@Override
	public Line read() throws StreamCorruptedException {
		if(_lines.isEmpty()) {
			processLine(this.readInput(), l -> _lines.add(l));
		}
		
		return _lines.isEmpty() ? null : _lines.pop();
	}

	private void processLine(Line line, Consumer<Line> consumer) {
		if(line != null) {
			int nrOfWords = line.getWords().size();
			for(int i = 0; i < nrOfWords; ++i) {
				Line newLine = new Line(line.getLineNumber());
				for(int j = 0; j < nrOfWords; ++j) {
					newLine.addWord(line.getWords().get( (i+j) % nrOfWords));
				}
				consumer.accept(newLine);
			}
		}
	}

	@Override
	public void write(Line line) throws StreamCorruptedException {
		if(line == null) {
			this.writeOutput(null);
		}
		
		processLine(line, l -> {
			try {
				this.writeOutput(l);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		});
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException();
	}
}
