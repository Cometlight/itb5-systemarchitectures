package itb5.filter;

import java.awt.image.renderable.ParameterBlock;
import java.security.InvalidParameterException;

import javax.media.jai.JAI;
import javax.media.jai.operator.MedianFilterDescriptor;

import itb5.types.ImageWrapper;
import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

/**
 * Performs the median filter processing, using the standard {@link javax.media.jai.operator.MedianFilterDescriptor#MEDIAN_MASK_SQUARE}.
 */
public class MedianOperator extends DataTransformationFilter<ImageWrapper> {
	private int _nrOfProcessingSteps;

	public MedianOperator(int nrOfProcessingSteps, Readable<ImageWrapper> input, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(input, output);
		_nrOfProcessingSteps = nrOfProcessingSteps;
	}
	
	public MedianOperator(int nrOfProcessingSteps, Readable<ImageWrapper> input)
			throws InvalidParameterException {
		super(input);
		_nrOfProcessingSteps = nrOfProcessingSteps;
	}
	
	public MedianOperator(int nrOfProcessingSteps, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(output);
		_nrOfProcessingSteps = nrOfProcessingSteps;
	}

	@Override
	protected void process(ImageWrapper entity) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(entity.getImage());
		pb.add(MedianFilterDescriptor.MEDIAN_MASK_SQUARE);
		pb.add(_nrOfProcessingSteps);
		entity.setImage(JAI.create("MedianFilter", pb));
	}

}
