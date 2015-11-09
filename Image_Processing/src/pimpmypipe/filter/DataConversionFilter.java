package pimpmypipe.filter;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;

import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public abstract class DataConversionFilter<in, out> extends AbstractFilter<in, out> {

	public DataConversionFilter(Readable<in> input, Writeable<out> output) throws InvalidParameterException {
		super(input, output);
	}
	
	public DataConversionFilter(Readable<in> input) throws InvalidParameterException {
		super(input);
	}
	
	public DataConversionFilter(Writeable<out> output) throws InvalidParameterException {
		super(output);
	}
	
	protected abstract out convert(in entity);

	@Override
	public out read() throws StreamCorruptedException {
		in data = this.readInput();
		if (data == null) {
			return null;
		} else {
			return convert(data);
		}
	}

	@Override
	public void write(in data) throws StreamCorruptedException {
		if (data == null) {
			this.writeOutput(null);
		} else {
			this.writeOutput(convert(data));
		}
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException();
	}
	
}
