package indsys.types;

public enum TextAlignment {
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    CENTER("CENTER");
    
    private String _name;
	
	TextAlignment(String name) {
		_name = name;
	}
	
	@Override
	public String toString() {
		return _name;
	}
}
