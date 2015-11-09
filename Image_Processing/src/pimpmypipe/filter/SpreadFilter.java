package pimpmypipe.filter;

import java.io.StreamCorruptedException;
import java.util.Arrays;
import java.util.Collection;

import pimpmypipe.interfaces.IOable;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class SpreadFilter<T> implements IOable<T, T> {
	private Readable<T> _in;
	private Collection<Writeable<T>> _outs;
	
	public SpreadFilter(Readable<T> in, Writeable<T>... outs) {
		_in = in;
		_outs = Arrays.asList(outs);
	}

	@Override
	public void write(T value) throws StreamCorruptedException {
		for(Writeable<T> out : _outs) {
			try {
				out.write(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public T read() throws StreamCorruptedException {
		return _in.read();
	}
	
	
	
}
