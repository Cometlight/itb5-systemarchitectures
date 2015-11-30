package beans;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.logging.Level;

import itb5.types.ImageWrapper;

/**
 * Performs the median filter processing, using the standard
 * {@link javax.media.jai.operator.MedianFilterDescriptor#MEDIAN_MASK_SQUARE}.
 */
public class MedianOperator extends PropertySupportBean {
	private static final long serialVersionUID = 1L;

	private int nrOfProcessingSteps;
	private ImageWrapper image;
	private ImageWrapper originalImage;

	public MedianOperator() {
		nrOfProcessingSteps = 1;
		image = null;
	}

	/**
	 * @see #setNrOfProcessingSteps(int)
	 * @return the number of processing steps
	 */
	public int getNrOfProcessingSteps() {
		return nrOfProcessingSteps;
	}

	/**
	 * Fires property change event "nrOfProcessingSteps"
	 * 
	 * @see #getNrOfProcessingSteps()
	 * @param nrOfProcessingSteps specifies how often the median filter shall be applied. Must be > 0
	 */
	public void setNrOfProcessingSteps(int nrOfProcessingSteps) {
		if (nrOfProcessingSteps != this.nrOfProcessingSteps && nrOfProcessingSteps > 0) {
			int oldValue = this.nrOfProcessingSteps;
			this.nrOfProcessingSteps = nrOfProcessingSteps;
			pcs.firePropertyChange("nrOfProcessingSteps", oldValue, nrOfProcessingSteps);
			process();
		}
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
		originalImage = newImageWrapper != null ? newImageWrapper.clone() : null;

		process();
	}

	
	private void process() {
		if (originalImage != null) {
			try {
				ImageWrapper oldImage = image;
				ImageWrapper newImage = new itb5.filter.MedianOperator(this.nrOfProcessingSteps,
						() -> originalImage.clone()).read();
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
