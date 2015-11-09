package itb5.filter;

import java.security.InvalidParameterException;

import javax.media.jai.PlanarImage;

import itb5.types.ImageWrapper;
import pimpmypipe.filter.DataConversionFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class ImageWrapperToPlanarImageConverter extends DataConversionFilter<ImageWrapper, PlanarImage> {

	public ImageWrapperToPlanarImageConverter(Readable<ImageWrapper> input, Writeable<PlanarImage> output)
			throws InvalidParameterException {
		super(input, output);
	}
	
	public ImageWrapperToPlanarImageConverter(Readable<ImageWrapper> input)
			throws InvalidParameterException {
		super(input);
	}
	
	public ImageWrapperToPlanarImageConverter(Writeable<PlanarImage> output)
			throws InvalidParameterException {
		super(output);
	}

	@Override
	protected PlanarImage convert(ImageWrapper entity) {
		return entity == null ? null : entity.getImage();
	}
}