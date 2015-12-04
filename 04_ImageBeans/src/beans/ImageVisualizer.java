package beans;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import itb5.types.ImageWrapper;

/**
 * Displays the received image and forwards it as "image".
 */
public class ImageVisualizer extends Canvas {
	private static final long serialVersionUID = 1L;

	private ImageWrapper image;
	private PropertyChangeSupport pcs;

	public ImageVisualizer() {
		super();
		this.pcs = new PropertyChangeSupport(this);
		image = null;
		setSize(100, 100);
		setBackground(Color.PINK);
	}

	@Override
	public void paint(Graphics g) {
		if (image != null) {
			g.drawImage(image.getImage().getAsBufferedImage(), 0, 0, this);
		}
	}

	public ImageWrapper getImage() {
		return image;
	}

	public void setImage(ImageWrapper newImageWrapper) {
		if (newImageWrapper != null && !newImageWrapper.equals(image)) {
			ImageWrapper oldImageWrapper = image;
			this.image = newImageWrapper.clone();
			repaint();
			pcs.firePropertyChange("image", oldImageWrapper, newImageWrapper);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
