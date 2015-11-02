package indsys.filter;

import java.security.InvalidParameterException;
import java.util.Arrays;

import indsys.types.Line;
import pimpmypipe.filter.DataEnrichmentFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

// TODO: split: reading in lines as a whole + split lines into words
public class LineBuilder extends DataEnrichmentFilter<String, Line> {
	private int _lineNumber;
	private final String _regex = "[^a-zA-Z0-9\\-']+";

	public LineBuilder(Readable<String> input, Writeable<Line> output) throws InvalidParameterException {
		super(input, output);
		_lineNumber = 1;
	}
	
	public LineBuilder(Writeable<Line> output) throws InvalidParameterException {
		super(output);
		_lineNumber = 1;
	}
	
	public LineBuilder(Readable<String> input) throws InvalidParameterException {
		super(input);
		_lineNumber = 1;
	}

	@Override
	protected boolean fillEntity(String nextVal, Line entity) {
		if(nextVal == null) {
			return true;
		}
		
		entity.setWords(Arrays.asList(nextVal.trim().split(_regex)));
		return true;
	}

	@Override
	protected Line getNewEntityObject() {
		return new Line(_lineNumber++);
	}

	@Override
	protected Line preSend(Line entity) {
		if(entity.getWords().isEmpty()) {
			return null;
		} else {
			return entity;
		}
	}
	
}
