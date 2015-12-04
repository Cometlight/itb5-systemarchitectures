package itb5.filter;

import java.awt.Rectangle;
import java.awt.image.renderable.ParameterBlock;
import java.security.InvalidParameterException;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import itb5.types.ImageWrapper;
import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

/**
 * Crops the image to the specified region of interest ({@link #_rectangle}).
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
		ParameterBlock pb = new ParameterBlock();
		
		// Ensure that rectangular crop area is not outside the image
		Rectangle adjustedRectangle = new Rectangle();
		adjustedRectangle.x = _rectangle.x < 0 ? 0 : _rectangle.x;
		adjustedRectangle.y = _rectangle.y < 0 ? 0 : _rectangle.y;
		adjustedRectangle.width = _rectangle.x + _rectangle.width < image.getWidth() ? _rectangle.width : image.getWidth() - _rectangle.x;
		adjustedRectangle.height = _rectangle.x + _rectangle.height < image.getHeight() ? _rectangle.height : image.getHeight() - _rectangle.y;
		
		pb.add(new Float(adjustedRectangle.x));
		pb.add(new Float(adjustedRectangle.y));
		pb.add(new Float(adjustedRectangle.getWidth()));
		pb.add(new Float(adjustedRectangle.getHeight()));
		pb.addSource(image);
		image = JAI.create("crop", pb);
		entity.setImage(image);
	}

}
