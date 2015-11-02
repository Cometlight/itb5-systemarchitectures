package indsys.filter;

import java.security.InvalidParameterException;

import indsys.types.Word;
import pimpmypipe.filter.DataEnrichmentFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class WordBuilder extends DataEnrichmentFilter<Character, Word> {
	private StringBuilder _currentWord;
	private final String _regex = "[a-zA-Z0-9\\-']";
	
	public WordBuilder(Readable<Character> input, Writeable<Word> output) throws InvalidParameterException {
		super(input, output);
		_currentWord = new StringBuilder();
	}
	
	public WordBuilder(Writeable<Word> output) throws InvalidParameterException {
		super(output);
		_currentWord = new StringBuilder();
	}
	
	public WordBuilder(Readable<Character> input) throws InvalidParameterException {
		super(input);
		_currentWord = new StringBuilder();
	}

	@Override
	protected boolean fillEntity(Character nextVal, Word entity) {
		if(nextVal == null) {
			return true;
		}

		String strVal = nextVal.toString();
		if(strVal.matches(_regex)) {
			_currentWord.append(strVal);
			return false;
		} else {
			if(_currentWord.length() == 0) {
				return false;
			} else {
				entity.setValue(_currentWord.toString());
				_currentWord.setLength(0);
				return true;
			}
		}
	}

	@Override
	protected Word getNewEntityObject() {
		return new Word();
	}
	
	@Override
	protected Word preSend(Word entity) {
		if(entity.getValue() == null) {
			return null;
		} else {
			return entity;
		}
	}

}
