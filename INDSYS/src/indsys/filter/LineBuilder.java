package indsys.filter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import indsys.types.Line;
import pimpmypipe.filter.DataEnrichmentFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class LineBuilder extends DataEnrichmentFilter<String, Line> {
	private int _lineNumber = 1;
	private final String _regex = "[^a-zA-Z0-9\\-']+";

	public LineBuilder(Readable<String> input, Writeable<Line> output) throws InvalidParameterException {
		super(input, output);
	}
	
	public LineBuilder(Writeable<Line> output) throws InvalidParameterException {
		super(output);
	}
	
	public LineBuilder(Readable<String> input) throws InvalidParameterException {
		super(input);
	}

	@Override
	protected boolean fillEntity(String nextVal, Line entity) {
		if(nextVal == null) {
			return true;
		}
		
		List<String> words = new ArrayList<>(Arrays.asList(nextVal.trim().split(_regex)));
		words.removeAll(Arrays.asList("", null));
		
		if(words.isEmpty()) {
			entity.setLineNumber(_lineNumber++);
			return false;
		}
		
		entity.setWords(words);
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
