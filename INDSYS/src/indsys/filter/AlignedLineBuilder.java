package indsys.filter;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

import indsys.types.MutableString;
import indsys.types.TextAlignment;
import pimpmypipe.filter.AbstractFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class AlignedLineBuilder extends AbstractFilter<MutableString, String> {
	private final String SPACER = " ";
	private StringBuilder _currentLine;
	private TextAlignment _textAlignment;
	private int _lineLength;

	public AlignedLineBuilder(TextAlignment textAlignment, int lineLength, Readable<MutableString> input,
			Writeable<String> output) throws InvalidParameterException {
		super(input, output);
		init(textAlignment, lineLength);
	}

	public AlignedLineBuilder(TextAlignment textAlignment, int lineLength, Writeable<String> output)
			throws InvalidParameterException {
		super(output);
		init(textAlignment, lineLength);
	}

	public AlignedLineBuilder(TextAlignment textAlignment, int lineLength, Readable<MutableString> input)
			throws InvalidParameterException {
		super(input);
		init(textAlignment, lineLength);
	}

	private void init(TextAlignment textAlignment, int lineLength) {
		_textAlignment = textAlignment;
		_lineLength = lineLength;
		_currentLine = new StringBuilder();
	}

	private String alignLine(String line) {
		line = line.trim();
		int nrOfSpaces = _lineLength - line.length();

		if (nrOfSpaces == 0) {
			return line;
		} else if (nrOfSpaces < 0) {
			// a word that is longer than the line
			nrOfSpaces = 0;
		}

		switch (_textAlignment) {
		case LEFT:
			return padLeft(line, nrOfSpaces);
		case RIGHT:
			return padRight(line, nrOfSpaces);
		case CENTER:
			return padRight(padLeft(line, nrOfSpaces / 2), nrOfSpaces / 2);
		default:
			return null;
		}
	}

	private String padLeft(String text, int nrOfSpaces) {
		String pad = new String(new char[nrOfSpaces]).replace('\0', ' ');
		return text + pad;
	}

	private String padRight(String text, int nrOfSpaces) {
		String pad = new String(new char[nrOfSpaces]).replace('\0', ' ');
		return pad + text;
	}

	@Override
	public String read() throws StreamCorruptedException {
		MutableString word;
		while ((word = this.readInput()) != null) {
			if (_currentLine.length() + word.getValue().length() <= _lineLength) {
				_currentLine.append(word.getValue()).append(SPACER);
			} else {
				String finalLine = _currentLine.toString();
				_currentLine.setLength(0);
				_currentLine.append(word.getValue()).append(SPACER);
				return alignLine(finalLine);
			}
		}
	
		// word == null
		if (_currentLine.length() > 0) {
			String finalLine = _currentLine.toString();
			_currentLine.setLength(0);
			return alignLine(finalLine);
		} else {
			return null;
		}
	}

	@Override
	public void write(MutableString word) throws StreamCorruptedException {
		if(word == null) {
			if (_currentLine.length() > 0) {
				String finalLine = _currentLine.toString();
				_currentLine.setLength(0);
				this.writeOutput(alignLine(finalLine));
			}
			this.writeOutput(null);
		} else {
			if (_currentLine.length() + word.getValue().length() <= _lineLength) {
				_currentLine.append(word.getValue()).append(SPACER);
			} else {
				String finalLine = _currentLine.toString();
				_currentLine.setLength(0);
				_currentLine.append(word.getValue()).append(SPACER);
				this.writeOutput(alignLine(finalLine));
			}
		}
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException();
	}

}
