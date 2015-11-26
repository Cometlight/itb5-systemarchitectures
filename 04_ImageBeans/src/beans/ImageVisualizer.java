package beans;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import itb5.types.ImageWrapper;

public class ImageVisualizer extends Canvas implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	private ImageWrapper image;
	private PropertyChangeSupport pcs;

	public ImageVisualizer() {
		super();
		image = null;
		pcs = new PropertyChangeSupport(this);
		setSize(100, 100);
		setBackground(Color.GRAY);
	}

	@Override
	public void paint(Graphics g) {
		if (image != null) {
			System.out.println("ImageVisualizer: " + image + " and " + image.getImage());
			g.drawImage(image.getImage().getAsBufferedImage(), 0, 0, this);
		} else {
			System.out.println("ImageVisualizer: it's null :C");
		}
	}

	public ImageWrapper getImage() {
		return image;
	}

	public void setImage(ImageWrapper newImageWrapper) {
		if (newImageWrapper != image) {
			ImageWrapper oldImageWrapper = image;
			this.image = newImageWrapper;
			repaint();
			pcs.firePropertyChange("image", oldImageWrapper, newImageWrapper);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		System.out.println(pce.getPropertyName() + ": " + pce.getOldValue() + " --> " + pce.getNewValue());
		if (pce.getPropertyName().equals("image")) {
			setImage((ImageWrapper) pce.getNewValue());
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
