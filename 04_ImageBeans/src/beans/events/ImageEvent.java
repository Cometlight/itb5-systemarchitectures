package beans.events;

import java.util.EventObject;

import itb5.types.ImageWrapper;

public class ImageEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private ImageWrapper value;

	public ImageEvent(Object source, ImageWrapper value) {
		super(source);
		this.value = value;
	}

	public ImageWrapper getValue() {
		return value;
	}

	public void setValue(ImageWrapper value) {
		this.value = value;
	}
}
