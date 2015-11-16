package itb5.filter;

import java.io.StreamCorruptedException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.JAI;

import itb5.types.ImageWrapper;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class ImageFileSource implements Readable<ImageWrapper> {
	private static final Logger _log = Logger.getLogger(ImageFileSource.class.getName());

	private String _filePath;
	private int _timesToRead;
	private ImageWrapper _imageRead;
	private Writeable<ImageWrapper> _out;

	public ImageFileSource(String filePath, int timesToRead) {
		_filePath = filePath;
		_timesToRead = timesToRead;
		_imageRead = new ImageWrapper(JAI.create("fileload", _filePath));
	}

	public ImageFileSource(String filePath, int timesToRead, Writeable<ImageWrapper> out) {
		this(filePath, timesToRead);
		_out = out;
		writeAll();
	}

	private void writeAll() {
		try {
			while (_timesToRead-- > 0) {
				_out.write(_imageRead);
			}
		} catch (StreamCorruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
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
