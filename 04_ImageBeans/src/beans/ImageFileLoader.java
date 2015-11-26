package beans;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.StreamCorruptedException;
import java.util.logging.Level;

import itb5.filter.ImageFileSource;
import itb5.types.ImageWrapper;

/**
 * Loads an image from the file system.
 */
public class ImageFileLoader extends PropertySupportBean {
	private static final long serialVersionUID = 1L;
	private String filename;
	private ImageWrapper image;

	public ImageFileLoader() {
		filename = "";
		image = null;
		pcs = new PropertyChangeSupport(this);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String newFilename) {
		if (newFilename != null && new File(newFilename).exists()) {
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
		if (newImageWrapper != null && !newImageWrapper.equals(image)) {
			ImageWrapper oldImageWrapper = image;
			image = newImageWrapper.clone();
			log.info("ImageFileLoader: setImage: firePropertyChange");
			pcs.firePropertyChange("image", oldImageWrapper, newImageWrapper);
		}
	}
	
	public ImageWrapper getImage() {
		return image == null ? null : image.clone();
	}
}
