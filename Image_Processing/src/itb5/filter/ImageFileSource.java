package itb5.filter;

import java.io.StreamCorruptedException;

import javax.media.jai.JAI;

import itb5.types.ImageWrapper;
import pimpmypipe.interfaces.Readable;

public class ImageFileSource implements Readable<ImageWrapper> {
	private String _filePath;
	private int _timesToRead;
	private ImageWrapper _imageRead;

	public ImageFileSource(String filePath, int timesToRead) {
		_filePath = filePath;
		_timesToRead = timesToRead;
		_imageRead = new ImageWrapper(JAI.create("fileload", _filePath));
	}

	@Override
	public ImageWrapper read() throws StreamCorruptedException {
		if (_timesToRead-- > 0) {
			return _imageRead;
		} else {
			return null;
		}
	}
}
