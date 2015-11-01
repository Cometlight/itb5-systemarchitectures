package indsys.filter;

import java.security.InvalidParameterException;

import indsys.types.Line;
import pimpmypipe.filter.DataEnrichmentFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

// TODO: split: reading in lines as a whole + split lines into words
public class LineBuilder extends DataEnrichmentFilter<Character, Line> {
	private int _lineNumber;
	private StringBuilder _currentWord;
	private final String _regex = "[a-zA-Z0-9\\-']";

	public LineBuilder(Readable<Character> input, Writeable<Line> output) throws InvalidParameterException {
		super(input, output);
		_lineNumber = 1;
		_currentWord = new StringBuilder();
	}

	@Override
	protected boolean fillEntity(Character nextVal, Line entity) {
		if(nextVal.equals('\n')) {
			entity.addWord(_currentWord.toString());
			_currentWord.setLength(0);
			return true;
		}
		
		String strVal = nextVal.toString();
		if(strVal.matches(_regex)) {
			_currentWord.append(strVal);
			return false;
		} else {
			entity.addWord(_currentWord.toString());
			_currentWord.setLength(0);
			return false;
		}
	}

	@Override
	protected Line getNewEntityObject() {
		return new Line(_lineNumber++);
	}

	
}
