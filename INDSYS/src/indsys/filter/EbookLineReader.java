package indsys.filter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;

import pimpmypipe.interfaces.Readable;

public class EbookLineReader implements Readable<String> {
	private FileReader _fileReader;
	private BufferedReader _bufferedReader;
	
	public EbookLineReader(String fileName) throws FileNotFoundException {
		_fileReader = new FileReader(fileName);
		_bufferedReader = new BufferedReader(_fileReader);
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
