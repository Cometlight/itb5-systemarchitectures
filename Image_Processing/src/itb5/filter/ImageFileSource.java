package itb5.filter;

import java.io.StreamCorruptedException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.JAI;

import itb5.types.ImageWrapper;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

/**
 * Reads the specified image from the file system and makes it available n 
 * times.
 */
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
				// return a clone of the image!
				_out.write(_imageRead.clone());
			}
			_out.write(null);
		} catch (StreamCorruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
	}

	@Override
	public ImageWrapper read() throws StreamCorruptedException {
		if (_timesToRead-- > 0) {
			// return a clone of the image.. we do not want them to work on the
			// same image twice
			return _imageRead.clone();
		} else {
			return null;
		}
	}
}
