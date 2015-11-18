package itb5.filter;

import java.io.StreamCorruptedException;

import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class AbstractSink<T> implements Writeable<T> {
	public AbstractSink() { }

	public AbstractSink(Readable<T> input) throws StreamCorruptedException {
		T entity;
		while( (entity = input.read()) != null) {
			System.out.println(entity);
		}
		System.out.println("done");
	}
	
	@Override
	public void write(T value) throws StreamCorruptedException {
		if(value != null) {
			System.out.println(value);
		}
	}
}
