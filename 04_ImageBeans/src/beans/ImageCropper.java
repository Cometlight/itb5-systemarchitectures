package beans;

import java.awt.Rectangle;
import java.util.logging.Level;

import itb5.types.ImageWrapper;

/**
 * Crops the image to the specified rectangle.
 */
public class ImageCropper extends PropertySupportBean {
	private static final long serialVersionUID = 1L;

	private int x;
	private int y;
	private int width;
	private int height;
	private ImageWrapper image;
	private ImageWrapper originalImage;

	public ImageCropper() {
		this.x = 0;
		this.y = 0;
		this.height = Integer.MAX_VALUE;
		this.width = Integer.MAX_VALUE;
		this.image = null;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int newX) {
		if (newX >= 0 && newX != x) {
			int oldX = x;
			x = newX;
			pcs.firePropertyChange("x", oldX, newX);
			process();
		}
	}

	public int getY() {
		return this.y;
	}

	public void setY(int newY) {
		if (newY >= 0 && newY != y) {
			int oldY = y;
			y = newY;
			pcs.firePropertyChange("y", oldY, newY);
			process();
		}
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int newHeight) {
		if (newHeight >= 0 && newHeight != height) {
			int oldHeight = height;
			height = newHeight;
			pcs.firePropertyChange("height", oldHeight, newHeight);
			process();
		}
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int newWidth) {
		if (newWidth >= 0 && newWidth != width) {
			int oldWidth = width;
			width = newWidth;
			pcs.firePropertyChange("width", oldWidth, newWidth);
			process();
		}
	}

	public ImageWrapper getImage() {
		return image;
	}

	public void setImage(ImageWrapper newImageWrapper) {
		originalImage = newImageWrapper != null ? newImageWrapper.clone() : null;
		process();
	}

	private void process() {
		if (originalImage != null) {
			try {
				ImageWrapper oldImage = image;
				ImageWrapper newImage = new itb5.filter.ImageCropper(new Rectangle(x, y, width, height),
						() -> originalImage.clone()).read();
				if (newImage != null) {
					image = newImage.clone();
				}
				pcs.firePropertyChange("image", oldImage, newImage);
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}
