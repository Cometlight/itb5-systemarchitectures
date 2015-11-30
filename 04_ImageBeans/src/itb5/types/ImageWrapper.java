package itb5.types;

import java.io.Serializable;

import javax.media.jai.PlanarImage;

/**
 * Wraps a {@link #javax.media.jai.PlanarImage}. This is useful when working
 * with PimpMyPipes.
 */
public class ImageWrapper implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
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
		return new ImageWrapper(_image);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_image == null) ? 0 : _image.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageWrapper other = (ImageWrapper) obj;
		if (_image == null) {
			if (other._image != null)
				return false;
		} else if (!_image.equals(other._image))
			return false;
		return true;
	}
}
