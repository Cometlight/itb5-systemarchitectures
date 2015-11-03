package indsys.filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;

import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class EbookLineReader implements Readable<String> {
	private FileReader _fileReader;
	private BufferedReader _bufferedReader;
	private Writeable<String> _out;
	
	public EbookLineReader(String fileName) throws FileNotFoundException {
		_fileReader = new FileReader(fileName);
		_bufferedReader = new BufferedReader(_fileReader);
	}
	
	public EbookLineReader(String fileName, Writeable<String> out) throws FileNotFoundException, StreamCorruptedException {
		this(fileName);
		_out = out;
		writeAll();
	}
	
	private void writeAll() throws StreamCorruptedException {
		try {
			String line;
			while( (line = _bufferedReader.readLine()) != null) {
				_out.write(line);
			}
			_out.write(null);	// end signal
		} catch (IOException e) {
			throw new StreamCorruptedException(e.getMessage());
		}
	}
	
	@Override
	public String read() throws StreamCorruptedException {
		try {
			return _bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
