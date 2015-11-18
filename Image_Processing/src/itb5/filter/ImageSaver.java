package itb5.filter;

import java.security.InvalidParameterException;

import javax.media.jai.JAI;

import itb5.types.ImageWrapper;
import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class ImageSaver extends DataTransformationFilter<ImageWrapper> {
	String _filePath;	// without file extension

	public ImageSaver(String filePath, Readable<ImageWrapper> input, Writeable<ImageWrapper> output) throws InvalidParameterException {
		super(input, output);
		_filePath = filePath;
	}
	
	public ImageSaver(String filePath, Readable<ImageWrapper> input) throws InvalidParameterException {
		super(input);
		_filePath = filePath;
	}
	
	public ImageSaver(String filePath, Writeable<ImageWrapper> output) throws InvalidParameterException {
		super(output);
		_filePath = filePath;
	}
	
	@Override
	protected void process(ImageWrapper entity) {
		JAI.create("filestore", entity.getImage(), _filePath + ".jpg", "jpeg", null);
	}

}
