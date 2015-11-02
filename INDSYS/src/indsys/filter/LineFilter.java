package indsys.filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

import indsys.types.Line;
import pimpmypipe.filter.DataEnrichmentFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class LineFilter extends DataEnrichmentFilter<Line, Line> {
	private static final String FILE_NAME = "uninteresting_words.txt";
	
	private Set<String> _uninterestingWords;

	public LineFilter(Readable<Line> input, Writeable<Line> output) throws InvalidParameterException {
		super(input, output);
		loadUninterestingWords();
	}
	
	public LineFilter(Readable<Line> input) throws InvalidParameterException {
		super(input);
		loadUninterestingWords();
	}
	
	public LineFilter(Writeable<Line> output) throws InvalidParameterException {
		super(output);
		loadUninterestingWords();
	}

	private void loadUninterestingWords() {
		_uninterestingWords = new HashSet<>();
		try {
			FileReader fileReader = new FileReader(FILE_NAME);
			@SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String word;
			while( (word = bufferedReader.readLine()) != null) {
				_uninterestingWords.add(word.toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean fillEntity(Line nextVal, Line entity) {
		if(nextVal == null) {
			return true;
		}
		
		if(_uninterestingWords.contains(nextVal.getFirstWord().toLowerCase())) {
			return false;
		}
		
		entity.copyFrom(nextVal);
		return true;
	}

	@Override
	protected Line getNewEntityObject() {
		return new Line(-1);
	}
	
	@Override
	protected Line preSend(Line entity) {
		if(entity.getLineNumber() < 0) {
			return null;
		} else {
			return entity;
		}
	}

}
