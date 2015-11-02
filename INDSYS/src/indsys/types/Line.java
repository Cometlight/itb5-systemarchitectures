package indsys.types;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Line implements Comparable<Line> {
	private int _lineNumber;
	private List<String> _words;

	public Line(int lineNumber) {
		_lineNumber = lineNumber;
		_words = new LinkedList<>();
	}

	public void addWord(String word) {
		if (word != null && word.length() > 0) {
			_words.add(word);
		}
	}

	public int getLineNumber() {
		return _lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		_lineNumber = lineNumber;
	}

	public List<String> getWords() {
		return _words;
	}

	public void setWords(List<String> words) {
		_words = words;
	}
	
	public String getFirstWord() {
		if(_words.isEmpty()) {
			return null;
		} else {
			return _words.get(0);
		}
	}
	
	public void copyFrom(Line other) {
		this._lineNumber = other._lineNumber;
		this._words = other._words;
	}

	@Override
	public String toString() {
		return _lineNumber + ": " + _words.toString();
	}

	@Override
	public int compareTo(Line o) {
		Objects.requireNonNull(o);

		int minSize = Math.min(_words.size(), o._words.size());
		for (int i = 0; i < minSize; ++i) {
			int compareValue = _words.get(i).compareToIgnoreCase(o._words.get(i));
			if (compareValue != 0) {
				return compareValue;
			}
		}
		return 0;
	}
}
