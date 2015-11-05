package indsys.types;

/**
 * Represents a mutable String.
 */
public class MutableString {
	private String _value;
	
	/**
	 * Creates a new mutable string and sets its initial value to the empty 
	 * string.
	 */
	public MutableString() {
		this("");
	}
	
	/**
	 * Creates a new mutable string and sets its initial value to the specified
	 * value.
	 * 
	 * @param initialValue the initial value of the string
	 */
	public MutableString(String initialValue) {
		this._value = initialValue;
	}
	
	/**
	 * Sets the value of the string.
	 * 
	 * @param newValue the new value of the string; may not be null
	 */
	public void setValue(String newValue) {
		this._value = newValue;
	}
	
	/**
	 * Gets the value of the string.
	 * 
	 * @return the value of the string
	 */
	public String getValue() {
		return this._value;
	}
}
