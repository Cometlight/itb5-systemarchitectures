package itb5.types;

import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

/**
 * Wraps a {@link #javax.media.jai.PlanarImage}. This is useful when working with PimpMyPipes.
 */
public class ImageWrapper implements Cloneable {
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
	
	@Override
	public ImageWrapper clone() {
		RenderedOp op = (RenderedOp)_image;
		return new ImageWrapper(op.createInstance());
	}
}
