package beans;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.logging.Level;

import itb5.filter.ImageSaver;
import itb5.types.ImageWrapper;

/**
 * Acts as a file saver. Supports property binding.
 */
public class ImageFileSaver extends PropertySupportBean {
	private static final long serialVersionUID = 1L;
	private ImageWrapper image;
	private String filename;
	
	public ImageFileSaver() {
		super();
		image = null;
		filename = "";
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
		image = newImage != null ? newImage.clone() : null;
		process();
		pcs.firePropertyChange("image", oldImage, newImage);
	}
	
	private void process() {
		if (image != null && filename != null && !filename.equals("")) {
			try {
				new ImageSaver(filename, () -> image).read();
			} catch (StreamCorruptedException | InvalidParameterException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
}
