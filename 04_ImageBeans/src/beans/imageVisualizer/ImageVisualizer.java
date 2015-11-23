package beans.imageVisualizer;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.LinkedList;

import beans.events.ImageEvent;
import beans.events.ImageListener;
import itb5.types.ImageWrapper;

public class ImageVisualizer extends Canvas implements ImageListener {
	private static final long serialVersionUID = 1L;
	private LinkedList<ImageListener> listeners = new LinkedList<>();
	private ImageWrapper imageWrapper;

	@Override
	public void imageValueChanged(ImageEvent event) {
		this.imageWrapper = event.getValue();
		repaint();
		notifyImageListener(event);
	}
	
	@Override
	public void paint(Graphics g) {
		if(imageWrapper != null) {
			System.out.println("ImageVisualizer: " + imageWrapper + " and " + imageWrapper.getImage());
			g.drawImage(imageWrapper.getImage().getAsBufferedImage(), 0, 0, this);
		} else {
			System.out.println("ImageVisualizer: it's null :C");
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
	protected void notifyImageListener(ImageEvent event) {
		LinkedList<ImageListener> listenersCopy;

		synchronized (this) {
			listenersCopy = (LinkedList<ImageListener>) listeners.clone();
		}

		listenersCopy.forEach(listener -> listener.imageValueChanged(event));
	}
}
