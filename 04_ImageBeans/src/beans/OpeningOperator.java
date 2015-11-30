package beans;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.logging.Level;

import itb5.JAIKernels;
import itb5.filter.ErodeDilateOperator.Mode;
import itb5.types.ImageWrapper;

public class OpeningOperator extends ErodeDilateOperator {
	private static final long serialVersionUID = 1L;

	@Override
	protected ImageWrapper getProcessedImage(ImageWrapper originalImage) {
		try {
			ImageWrapper eroded = new itb5.filter.ErodeDilateOperator(
					Mode.erode,
					JAIKernels.circle7,
					this.nrOfProcessingCycles,
					() -> originalImage.clone()).read();
			ImageWrapper dilated = new itb5.filter.ErodeDilateOperator(
					Mode.dilate,
					JAIKernels.circle7,
					this.nrOfProcessingCycles,
					() -> eroded).read();
			return dilated;
		} catch (StreamCorruptedException | InvalidParameterException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return originalImage;
		}
	}
	
}
