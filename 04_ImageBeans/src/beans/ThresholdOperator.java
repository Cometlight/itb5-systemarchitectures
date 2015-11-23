package beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import itb5.types.ImageWrapper;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

/**
 * All pixels whose brightness is between {@link #low} and {@link #high} are
 * changed to the color specified in {@link #map}.
 * 
 * TODO: bind image and update image on property change
 */
public class ThresholdOperator implements Serializable, PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	private double low;
	private double high;
	private double map;
	private ImageWrapper imageWrapper;

	private PropertyChangeSupport pcs;

	public ThresholdOperator() {
		low = 0.0;
		high = 0.0;
		map = 255.0;
		imageWrapper = null;
		pcs = new PropertyChangeSupport(this);
	}

	public double getLow() {
		return low;
	}

	public void setLow(double newLow) {
		if (newLow != low) {
			double oldLow = low;
			low = newLow;
			pcs.firePropertyChange("low", oldLow, newLow);
			process();
		}
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double newHigh) {
		if (newHigh != high) {
			double oldHigh = high;
			high = newHigh;
			pcs.firePropertyChange("high", oldHigh, newHigh);
			process();
		}
	}

	public double getMap() {
		return map;
	}

	public void setMap(double newMap) {
		if (newMap != map) {
			double oldMap = map;
			map = newMap;
			pcs.firePropertyChange("map", oldMap, newMap);
			process();
		}
	}

	public ImageWrapper getImageWrapper() {
		return imageWrapper;
	}

	public void setImageWrapper(ImageWrapper newImageWrapper) {
		if (newImageWrapper != null) {
			imageWrapper = newImageWrapper.clone();
		} else {
			imageWrapper = null;
		}
		process();
	}

	private void process() {
		if (imageWrapper != null) {
			new itb5.filter.ThresholdOperator(low, high, map, new Readable<ImageWrapper>() {
				@Override
				public ImageWrapper read() throws StreamCorruptedException {
					return imageWrapper;
				}
			}, new Writeable<ImageWrapper>() {
				@Override
				public void write(ImageWrapper value) throws StreamCorruptedException {
					imageWrapper = value;
					pcs.firePropertyChange("image", null, imageWrapper);
				}
			});
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		// TODO ...
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
