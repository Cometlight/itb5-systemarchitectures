package beans;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.logging.Level;

import itb5.JAIKernels;
import itb5.filter.ErodeDilateOperator.Mode;
import itb5.types.ImageWrapper;

public class ErodeOperator extends ErodeDilateOperator {
	private static final long serialVersionUID = 1L;

	@Override
	protected ImageWrapper getProcessedImage(ImageWrapper originalImage) {
		try {
			return new itb5.filter.ErodeDilateOperator(
					Mode.erode,
					JAIKernels.circle5,
					this.nrOfProcessingCycles,
					() -> originalImage.clone()).read();
		} catch (StreamCorruptedException | InvalidParameterException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
			return originalImage;
		}
	}

}
