package indsys.filter;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

import indsys.types.TextAlignment;
import indsys.types.Word;
import pimpmypipe.filter.AbstractFilter;
import pimpmypipe.filter.DataEnrichmentFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class AlignedLineBuilder extends AbstractFilter<Word, String> {
	private final String SPACER = " ";
	private StringBuilder _currentLine;
	private TextAlignment _textAlignment;
	private int _lineLength;

	public AlignedLineBuilder(TextAlignment textAlignment, int lineLength, Readable<Word> input,
			Writeable<String> output) throws InvalidParameterException {
		super(input, output);
		_textAlignment = textAlignment;
		_lineLength = lineLength;
		_currentLine = new StringBuilder();
	}

	public AlignedLineBuilder(TextAlignment textAlignment, int lineLength, Writeable<String> output)
			throws InvalidParameterException {
		super(output);
		_textAlignment = textAlignment;
		_lineLength = lineLength;
		_currentLine = new StringBuilder();
	}

	public AlignedLineBuilder(TextAlignment textAlignment, int lineLength, Readable<Word> input)
			throws InvalidParameterException {
		super(input);
		_textAlignment = textAlignment;
		_lineLength = lineLength;
		_currentLine = new StringBuilder();
	}

	@Override
	public String read() throws StreamCorruptedException {
		Word word;
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
	
	private String alignLine(String line) {
		line = line.trim();
		int nrOfSpaces = _lineLength - line.length();

		if (nrOfSpaces == 0) {
			return line;
		}

		switch (_textAlignment) {
		case Left:
			return padLeft(line, nrOfSpaces);
		case Right:
			return padRight(line, nrOfSpaces);
		case Center:
			return padRight(padLeft(line, nrOfSpaces / 2), nrOfSpaces / 2);
		default:
			return null;
		}
	}

	private String padLeft(String text, int nrOfSpaces) {
		String pad = "";
		for(int i = 0; i < nrOfSpaces; ++i) {
			pad += " ";
		}
		return text + pad;
	}

	private String padRight(String text, int nrOfSpaces) {
		String pad = "";
		for(int i = 0; i < nrOfSpaces; ++i) {
			pad += " ";
		}
		return pad + text;
	}

	@Override
	public void write(Word word) throws StreamCorruptedException {
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
		// TODO Auto-generated method stub

	}

}
