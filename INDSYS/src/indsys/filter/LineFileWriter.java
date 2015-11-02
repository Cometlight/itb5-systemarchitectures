package indsys.filter;

import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;

import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class LineFileWriter extends DataTransformationFilter<String> {
	private FileWriter _fileWriter;

	public LineFileWriter(String fileName, Readable<String> input, Writeable<String> output)
			throws InvalidParameterException {
		super(input, output);
		initFileWriter(fileName);
	}

	public LineFileWriter(String fileName, Readable<String> input) throws InvalidParameterException {
		super(input);
		initFileWriter(fileName);
	}

	public LineFileWriter(String fileName, Writeable<String> output) throws InvalidParameterException {
		super(output);
		initFileWriter(fileName);
	}

	private void initFileWriter(String fileName) {
		try {
			_fileWriter = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process(String entity) {
		if (entity == null) {
			if (_fileWriter != null) {
				try {
					_fileWriter.flush();
					_fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				_fileWriter = null;
			}
		} else {
			try {
				_fileWriter.write(entity + System.lineSeparator());
				_fileWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
