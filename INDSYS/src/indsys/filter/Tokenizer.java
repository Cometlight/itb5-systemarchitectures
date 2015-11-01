package indsys.filter;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

import pimpmypipe.filter.AbstractFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class Tokenizer extends AbstractFilter<Character, String> {
	private StringBuilder _currentToken;
	private final String _regex = "[a-zA-Z0-9\\-]";
	
	public Tokenizer(Readable<Character> input, Writeable<String> output) throws InvalidParameterException {
		super(input, output);
		_currentToken = new StringBuilder();
	}

	@Override
	public String read() throws StreamCorruptedException {
		if(_currentToken == null) {
			return null;
		}
		
		if(_currentToken.length() > 0 && !_currentToken.toString().matches(_regex)) {
			String returnToken = _currentToken.toString();
			_currentToken.setLength(0);
			return returnToken;
		}
		
		while(true) {
			Character ch = this.readInput();
			if(ch == null) {
				if(_currentToken.length() > 0) {
					return _currentToken.toString();
				} else {
					_currentToken = null;
					return null;
				}
			}
			String curChar = ch.toString();
			if(curChar.matches(_regex)) {
				_currentToken.append(curChar);
			} else {
				if(_currentToken.length() > 0) {
					String returnToken = _currentToken.toString();
					_currentToken.setLength(0);
					_currentToken.append(curChar);
					return returnToken;
				} else {
					return curChar;
				}
			}
		}
	}

	@Override
	public void write(Character value) throws StreamCorruptedException {
		if (value != null) {
			String str = value.toString();
			if(str.matches(_regex)) {
				_currentToken.append(str);
			} else {
				if(_currentToken.length() > 0) {
					this.writeOutput(_currentToken.toString());
					_currentToken.setLength(0);	// clear
				}
				this.writeOutput(str);
			}
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
