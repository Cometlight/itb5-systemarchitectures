package indsys.types;

public class Word {
	private String _value;

	public Word() {
	}

	public Word(String value) {
		_value = value;
	}

	public void setValue(String value) {
		_value = value;
	}

	public String getValue() {
		return _value;
	}
}
