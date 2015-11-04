package indsys;

import java.io.StreamCorruptedException;

import pimpmypipe.interfaces.Writeable;
import pimpmypipe.interfaces.Readable;

public class ConsoleSink<T> implements Writeable<T> {

	public ConsoleSink() {
	}

	public ConsoleSink(Readable<T> readable) throws StreamCorruptedException {
		T line;
		while ((line = readable.read()) != null) {
			System.out.println(line);
		}
	}

	@Override
	public void write(T line) throws StreamCorruptedException {
		System.out.println(line);
	}

}
