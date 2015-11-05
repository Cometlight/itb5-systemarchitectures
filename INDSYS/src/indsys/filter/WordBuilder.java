package indsys.filter;

import java.security.InvalidParameterException;

import indsys.types.MutableString;
import pimpmypipe.filter.DataEnrichmentFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class WordBuilder extends DataEnrichmentFilter<Character, MutableString> {
	private StringBuilder _currentWord = new StringBuilder();
	private final String _regex = "[a-zA-Z0-9\\-:\\.,;']";
	
	public WordBuilder(Readable<Character> input, Writeable<MutableString> output) throws InvalidParameterException {
		super(input, output);
	}
	
	public WordBuilder(Writeable<MutableString> output) throws InvalidParameterException {
		super(output);
	}
	
	public WordBuilder(Readable<Character> input) throws InvalidParameterException {
		super(input);
	}

	@Override
	protected boolean fillEntity(Character nextVal, MutableString entity) {
		if(nextVal == null) {
			entity.setValue(null);
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
	protected MutableString getNewEntityObject() {
		return new MutableString();
	}
	
	@Override
	protected MutableString preSend(MutableString entity) {
		if(entity.getValue() == null) {
			return null;
		} else {
			return entity;
		}
	}

}
