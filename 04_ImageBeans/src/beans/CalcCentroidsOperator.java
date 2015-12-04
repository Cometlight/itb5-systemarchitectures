package beans;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.logging.Level;

import itb5.types.Coordinate;
import itb5.types.ImageWrapper;

/**
 * Calculates the centroids of objects on the image.
 */
public class CalcCentroidsOperator extends PropertySupportBean {
	private static final long serialVersionUID = 1L;
	
	private ImageWrapper image;
	private LinkedList<Coordinate> centroids;
	
	public CalcCentroidsOperator() {
		image = null;
		centroids = null;
	}
	
	/**
	 * @return the centroids of the objects in the image
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<Coordinate> getCentroids() {
		if (centroids == null) {
			return null;
		}
		return (LinkedList<Coordinate>) centroids.clone();
	}
	
	/**
	 * This should not be called from the outside!
	 * Fires property change event "centroids".
	 * @param newCentroids
	 */
	public void setCentroids(LinkedList<Coordinate> newCentroids) {
		LinkedList<Coordinate> oldCentroids = centroids;
		centroids = newCentroids;
		System.out.println(centroids);
		pcs.firePropertyChange("centroids", oldCentroids, newCentroids);
	}

	/**
	 * @see #setImage(ImageWrapper)
	 * @return the processed image
	 */
	public ImageWrapper getImage() {
		return image;
	}

	/**
	 * Fires property change event "image", if the image given is valid.
	 * 
	 * @see #getImage()
	 * @param newImageWrapper the new image to be processed.
	 */
	public void setImage(ImageWrapper newImageWrapper) {
		image = newImageWrapper != null ? newImageWrapper.clone() : null;
		pcs.firePropertyChange("image", null, image);
		process();
	}

	
	private void process() {
		if (image != null) {
			try {
				setCentroids(new itb5.filter.CalcCentroidsFilter(() -> image.clone().getImage()).read());
			} catch (StreamCorruptedException | InvalidParameterException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
}
