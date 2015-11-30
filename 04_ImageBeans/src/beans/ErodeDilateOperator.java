package beans;

import itb5.types.ImageWrapper;

public abstract class ErodeDilateOperator extends PropertySupportBean {
	private static final long serialVersionUID = 1L;

	protected int nrOfProcessingCycles;
	private ImageWrapper image;
	private ImageWrapper originalImage;
	
	protected ErodeDilateOperator() {
		this.nrOfProcessingCycles = 1;
	}

	/**
	 * @return the nrOfProcessingCycles
	 */
	public int getNrOfProcessingCycles() {
		return nrOfProcessingCycles;
	}

	/**
	 *  Fires property change event "nrOfProcessingCycles"
	 * 
	 * @param nrOfProcessingCycles the nrOfProcessingCycles to set, must be > 0
	 */
	public void setNrOfProcessingCycles(int nrOfProcessingCycles) {
		if (nrOfProcessingCycles != this.nrOfProcessingCycles && nrOfProcessingCycles > 0) {
			int oldValue = this.nrOfProcessingCycles;
			this.nrOfProcessingCycles = nrOfProcessingCycles;
			pcs.firePropertyChange("nrOfProcessingCycles", oldValue, nrOfProcessingCycles);
			process();
		}
	}
	
	/**
	 * @return the image
	 */
	public ImageWrapper getImage() {
		return image;
	}

	/**
	 * Fires property change event "image", if the image given is valid.
	 * 
	 * @param image the image to set
	 */
	public void setImage(ImageWrapper image) {
		originalImage = image != null ? image.clone() : null;

		process();
	}
	
	protected abstract ImageWrapper getProcessedImage(ImageWrapper originalImage);
	
	private void process() {
		if (originalImage != null) {
			ImageWrapper oldImage = image;
			ImageWrapper newImage = getProcessedImage(originalImage.clone());
			if (newImage != null) {
				image = newImage.clone();
			}
			pcs.firePropertyChange("image", oldImage, newImage);
		}
	}
	
}
