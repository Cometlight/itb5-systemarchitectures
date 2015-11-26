package beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.logging.Level;
import java.util.logging.Logger;

import itb5.filter.ImageFileSource;
import itb5.types.ImageWrapper;

/**
 * Loads an image from the file system.
 */
public class ImageFileLoader implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ImageFileLoader.class.getName());

	private String filename;
	private ImageWrapper image;
	private PropertyChangeSupport pcs;

	public ImageFileLoader() {
		filename = "";
		image = null;
		pcs = new PropertyChangeSupport(this);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String newFilename) {
		if (new File(newFilename).exists()) {
			String oldFilename = filename;
			filename = newFilename;
			try {
				log.info("ImageFileLoader: Trying to make an image...");
				ImageWrapper imgWrapper = new ImageFileSource(newFilename, 1).read();
				log.info("ImageFileLoader: ImageWrapper = " + imgWrapper);
				pcs.firePropertyChange("filename", oldFilename, newFilename);
				this.setImage(imgWrapper);
			} catch (StreamCorruptedException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	public void setImage(ImageWrapper newImageWrapper) {
		log.info("ImageFileLoader: setImage: " + newImageWrapper);
		if (newImageWrapper != image) {
			ImageWrapper oldImageWrapper = image;
			image = newImageWrapper;
			log.info("ImageFileLoader: setImage: firePropertyChange");
			pcs.firePropertyChange("image", oldImageWrapper, newImageWrapper);
		}
	}
	
	public ImageWrapper getImgae() {
		return image == null ? null : image.clone();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
