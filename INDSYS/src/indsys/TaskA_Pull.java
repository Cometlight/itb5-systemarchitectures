package indsys;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import indsys.filter.EbookLineReader;
import indsys.filter.LineBuilder;
import indsys.filter.LineFilter;
import indsys.filter.LineSorter;
import indsys.filter.LineSpinner;
import indsys.filter.LineToString;
import indsys.types.Line;
import pimpmypipe.interfaces.Readable;

public class TaskA_Pull {
	private static final Logger _log = Logger.getLogger(TaskA_Pull.class.getName());

	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Please provide the file name of the source text as parameter.");
			return;
		}
		String sourceFileName = args[0];
		
		if(!new File(sourceFileName).exists()) {
			System.out.println("'" + sourceFileName + "' not found. Please provide a valid file name of the source text.");
			return;
		}
		
		try {
			EbookLineReader ebookLineReader = new EbookLineReader(sourceFileName);
			LineBuilder lineBuilder = new LineBuilder(ebookLineReader);
			LineSpinner lineSpinner = new LineSpinner(lineBuilder);
			LineFilter lineFilter = new LineFilter((Readable<Line>)lineSpinner);
			LineSorter lineSorter = new LineSorter((Readable<Line>)lineFilter);
			LineToString lineToString = new LineToString(lineSorter);
			ConsoleSink<String> consoleSink = new ConsoleSink<>(lineToString);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
