package indsys;

/**
 * Represents a mutable String. Its value can never be null.
 */
public class MutableString {
	private String _value;
	
	/**
	 * Creates a new mutable string and sets its initial value to the empty string.
	 */
	public MutableString() {
		this("");
	}
	
	/**
	 * Creates a new mutable string and sets its initial value to the specified value.
	 * 
	 * @param initialValue the initial value of the string; may not be null.
	 */
	public MutableString(String initialValue) {
		if (initialValue != null) {
			this._value = initialValue;
		} else {
			this._value = "";
		}
	}
	
	/**
	 * Sets the value of the string.
	 * 
	 * @param newValue the new value of the string; may not be null.
	 */
	public void setValue(String newValue) {
		if (newValue != null) {
			this._value = newValue;
		}
	}
	
	/**
	 * Gets the value of the string.
	 * 
	 * @return the value of the string; it is ensured that the value is not null.
	 */
	public String getValue() {
		return this._value;
	}
}
