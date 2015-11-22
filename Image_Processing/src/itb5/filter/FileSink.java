package itb5.filter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

/**
 * A sink that only prints the received values into a file.
 */
public class FileSink<T> implements Writeable<T> {
	private static final Logger _log = Logger.getLogger(FileSink.class.getName());

	private FileWriter _fileWriter;
	private Readable<T> _input;

	public FileSink(String fileName) {
		initFileWriter(fileName);
	}

	public FileSink(String fileName, Readable<T> input) throws StreamCorruptedException {
		this(fileName);
		_input = input;
	}
	
	// Note: This method wasn't named "run" to avoid confusion with the Runnable interface.
	// This method may only be used if a Readable<> has been provided in the constructor.
	public void readAll() {
		if(_input != null) {
			try {
				T entity;
				while ((entity = _input.read()) != null) {
					_fileWriter.write(entity.toString() + System.lineSeparator());
				}
				_fileWriter.flush();
				_fileWriter.close();
			} catch (IOException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	private void initFileWriter(String fileName) {
		try {
			_fileWriter = new FileWriter(fileName);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public void write(T value) throws StreamCorruptedException {
		if (_fileWriter != null) {
			try {
				if (value != null) {
					_fileWriter.write(value.toString() + System.lineSeparator());
				} else {
					_fileWriter.flush();
					_fileWriter.close();
					_fileWriter = null;
				}
			} catch (IOException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}