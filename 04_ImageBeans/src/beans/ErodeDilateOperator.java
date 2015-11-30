package beans;

import itb5.types.ImageWrapper;

public abstract class ErodeDilateOperator extends PropertySupportBean {
	private static final long serialVersionUID = 1L;

	protected int nrOfProcessingCycles;
	private ImageWrapper image;
	private ImageWrapper originalImage;
//	protected Mode mode;
	
	protected ErodeDilateOperator(/*Mode mode*/) {
		this.nrOfProcessingCycles = 1;
//		this.mode = mode;
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

//	private void process() {
//		if (originalImage != null && mode != null) {
//			try {
//				ImageWrapper oldImage = image;
//				ImageWrapper newImage = new itb5.filter.ErodeDilateOperator(
//						mode,
//						JAIKernels.circle7,
//						this.nrOfProcessingCycles,
//						() -> originalImage.clone()).read();
//				if (newImage != null) {
//					image = newImage.clone();
//				}
//				pcs.firePropertyChange("image", oldImage, newImage);
//			} catch (StreamCorruptedException | InvalidParameterException e) {
//				log.log(Level.SEVERE, e.getMessage(), e);
//			}
//		}
//	}
	
}
