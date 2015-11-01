package indsys.filter;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

import indsys.types.Line;
import pimpmypipe.filter.AbstractFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class LineToString extends AbstractFilter<Line, String> {

	public LineToString(Readable<Line> input, Writeable<String> output) throws InvalidParameterException {
		super(input, output);
	}

	@Override
	public String read() throws StreamCorruptedException {
		Line line = this.readInput();
		if(line == null) {
			return null;
		} else {
			StringBuilder strB = new StringBuilder();
			line.getWords().forEach(word -> strB.append(word).append(" "));
			strB.append(" - ").append(line.getLineNumber());
			return strB.toString();
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
