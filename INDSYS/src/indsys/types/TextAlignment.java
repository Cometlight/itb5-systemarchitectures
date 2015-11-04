package indsys.types;

public enum TextAlignment {
    Left("Left"),
    Right("Right"),
    Center("Center");
    
    private String _name;
	
	TextAlignment(String name) {
		_name = name;
	}
	
	@Override
	public String toString() {
		return _name;
	}
}
