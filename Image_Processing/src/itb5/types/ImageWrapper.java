package itb5.types;

import javax.media.jai.PlanarImage;

public class ImageWrapper {
	private PlanarImage _image;
	
	public ImageWrapper(PlanarImage image) {
		_image = image;
	}

	public PlanarImage getImage() {
		return _image;
	}

	public void setImage(PlanarImage _image) {
		this._image = _image;
	}
}
