package indsys.filter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;

import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class EbookReader implements Readable<Character> {
	private FileReader _fileReader;
	private Writeable<Character> _out;
	
	public EbookReader(String fileName) throws FileNotFoundException {
		_fileReader = new FileReader(fileName);
	}
	
	public EbookReader(String fileName, Writeable<Character> out) throws FileNotFoundException, StreamCorruptedException {
		this(fileName);
		_out = out;
		writeAll();
	}
	
	private void writeAll() throws StreamCorruptedException {
		try {
			int value;
			while( (value = _fileReader.read()) >= 0) {
				_out.write(Character.valueOf((char)value));
			}
			_out.write(null);	// end signal
		} catch (IOException e) {
			throw new StreamCorruptedException(e.getMessage());
		}
	}

	@Override
	public Character read() throws StreamCorruptedException {
		try {
			int value = _fileReader.read();
			if(value < 0) {
				return null;
			}
			return Character.valueOf((char)value);
		} catch (IOException e) {
			throw new StreamCorruptedException(e.getMessage());
		}
	}

}
