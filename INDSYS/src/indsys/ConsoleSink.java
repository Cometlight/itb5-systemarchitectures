package indsys;

import java.io.StreamCorruptedException;

import pimpmypipe.interfaces.Writeable;
import pimpmypipe.interfaces.Readable;

/**
 * Takes objects of T and prints them to the console.
 * 
 * @param <T> the type of objects that are printed to the console.
 */
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
