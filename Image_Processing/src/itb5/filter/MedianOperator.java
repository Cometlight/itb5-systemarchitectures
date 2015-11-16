package itb5.filter;

import java.awt.image.renderable.ParameterBlock;
import java.security.InvalidParameterException;

import javax.media.jai.JAI;
import javax.media.jai.operator.MedianFilterDescriptor;

import itb5.types.ImageWrapper;
import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class MedianOperator extends DataTransformationFilter<ImageWrapper> {

	public MedianOperator(Readable<ImageWrapper> input, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(input, output);
	}
	
	public MedianOperator(Readable<ImageWrapper> input)
			throws InvalidParameterException {
		super(input);
	}
	
	public MedianOperator(Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(output);
	}

	@Override
	protected void process(ImageWrapper entity) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(entity.getImage());
		pb.add(MedianFilterDescriptor.MEDIAN_MASK_SQUARE);
		pb.add(3);
		entity.setImage(JAI.create("MedianFilter", pb));
	}

}
