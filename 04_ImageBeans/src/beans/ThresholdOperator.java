package beans;

import java.beans.PropertyChangeSupport;
import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.logging.Level;

import itb5.types.ImageWrapper;

/**
 * All pixels whose brightness is between {@link #low} and {@link #high} are
 * changed to the color specified in {@link #map}.
 * 
 * TODO: bind image and update image on property change
 */
public class ThresholdOperator extends PropertySupportBean {
	private static final long serialVersionUID = 1L;

	private double low;
	private double high;
	private double map;
	private ImageWrapper image;
	private ImageWrapper originalImage;

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

	public ImageWrapper getImage() {
		return image;
	}

	public void setImage(ImageWrapper newImageWrapper) {
		originalImage = newImageWrapper != null ? newImageWrapper.clone() : null;
		
		process();
	}

	private void process() {
		if (originalImage != null) {
			try {
				ImageWrapper oldImage = image;
				ImageWrapper newImage = new itb5.filter.ThresholdOperator(low, high, map, () -> originalImage.clone()).read();
				if (newImage != null) {
					image = newImage.clone();
				}
				pcs.firePropertyChange("image", oldImage, newImage);
			} catch (StreamCorruptedException | InvalidParameterException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

}
