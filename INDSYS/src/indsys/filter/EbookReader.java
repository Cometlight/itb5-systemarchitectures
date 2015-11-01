package indsys.filter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;

import pimpmypipe.interfaces.Readable;

public class EbookReader implements Readable<Character> {
	private FileReader _fileReader;
	
	public EbookReader(String fileName) throws FileNotFoundException {
		_fileReader = new FileReader(fileName);
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
