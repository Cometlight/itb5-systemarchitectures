package beans.imageFileLoader;

import java.io.File;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import beans.events.ImageEvent;
import beans.events.ImageListener;
import itb5.filter.ImageFileSource;
import itb5.types.ImageWrapper;

public class ImageFileLoader implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ImageFileLoader.class.getName());

	private String fileName;
	private LinkedList<ImageListener> listeners;

	public ImageFileLoader() {
		fileName = "";
		listeners = new LinkedList<>();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		// TODO: verify if loadable by JAI
		if (new File(fileName).exists()) {
			this.fileName = fileName;
			try {
				ImageWrapper imgWrp = new ImageFileSource(fileName, 1).read();
				System.out.println("ImageFileLoader: " + imgWrp);
				notifyImageListener(imgWrp);
			} catch (StreamCorruptedException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void addImageListener(ImageListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public void removeImageListener(ImageListener listener) {
		listeners.remove(listener);
	}

	@SuppressWarnings("unchecked")
	protected void notifyImageListener(ImageWrapper value) {
		LinkedList<ImageListener> listenersCopy;
		ImageEvent event = new ImageEvent(this, value);

		synchronized (this) {
			listenersCopy = (LinkedList<ImageListener>) listeners.clone();
		}

		listenersCopy.forEach(listener -> listener.imageValueChanged(event));
	}
}
