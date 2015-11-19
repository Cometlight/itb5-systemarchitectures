package itb5.filter;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.security.InvalidParameterException;

import javax.media.jai.PlanarImage;

import itb5.types.ImageWrapper;
import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

/**
 * Crops the image to the specified region of interest.
 */
public class ImageCropper extends DataTransformationFilter<ImageWrapper> {
	private Rectangle _rectangle;

	public ImageCropper(Rectangle rectangle, Readable<ImageWrapper> input, Writeable<ImageWrapper> output) throws InvalidParameterException {
		super(input, output);
		_rectangle = rectangle;
	}
	
	public ImageCropper(Rectangle rectangle, Readable<ImageWrapper> input) throws InvalidParameterException {
		super(input);
		_rectangle = rectangle;
	}
	
	public ImageCropper(Rectangle rectangle, Writeable<ImageWrapper> output) throws InvalidParameterException {
		super(output);
		_rectangle = rectangle;
	}

	@Override
	protected void process(ImageWrapper entity) {
		PlanarImage image = entity.getImage();
		image = PlanarImage.wrapRenderedImage((RenderedImage)image.getAsBufferedImage(_rectangle, image.getColorModel()));
		entity.setImage(image);
	}
	
	

}
