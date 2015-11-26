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
	private ImageWrapper image;

	private PropertyChangeSupport pcs;

	public ThresholdOperator() {
		low = 0.0;
		high = 0.0;
		map = 255.0;
		image = null;
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
			System.out.println("Threshold: new low: " + low);
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
			System.out.println("Threshold: new high: " + high);
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
			System.out.println("Threshold: new map: " + map);
			process();
		}
	}

	public ImageWrapper getImage() {
		return image;
	}

	public void setImage(ImageWrapper newImageWrapper) {
		System.out.println("Threshold: setImage: " + newImageWrapper);
		if (newImageWrapper != null) {
			image = newImageWrapper;
		} else {
			image = null;
		}
		process();
	}

	private void process() {
		System.out.println("Threshold: processing...: " + image);
		if (image != null) {
			new itb5.filter.ThresholdOperator(low, high, map, new Readable<ImageWrapper>() {
				@Override
				public ImageWrapper read() throws StreamCorruptedException {
					return image;
				}
			}, new Writeable<ImageWrapper>() {
				@Override
				public void write(ImageWrapper value) throws StreamCorruptedException {
					image = value;
					System.out.println("Threshold: new image: " + value);
					pcs.firePropertyChange("image", null, image);
				}
			});
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		System.out.println(pce.getPropertyName() + ": " + pce.getOldValue() + " --> " + pce.getNewValue());
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
