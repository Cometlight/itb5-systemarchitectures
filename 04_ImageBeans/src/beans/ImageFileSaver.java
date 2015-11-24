package beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import itb5.types.ImageWrapper;

/**
 * Acts as a file saver. Supports property binding.
 */
public class ImageFileSaver implements Serializable, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private ImageWrapper image;
	private String filename;
	private PropertyChangeSupport pcs;
	
	public ImageFileSaver() {
		image = null;
		filename = "";
		pcs = new PropertyChangeSupport(this);
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String newFilename) {
		String oldFilename = filename;
		filename = newFilename;
		pcs.firePropertyChange("filename", oldFilename, newFilename);
		process();
	}
	
	public ImageWrapper getImage() {
		return image;
	}
	
	public void setImage(ImageWrapper newImage) {
		ImageWrapper oldImage = image;
		image = newImage.clone();
		pcs.firePropertyChange("image", oldImage, newImage);
		process();
	}
	
	private void process() {
		if (this.image != null && !filename.equals("")) {
			//TODO
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.setImage((ImageWrapper)evt.getNewValue());
	}

	
}
