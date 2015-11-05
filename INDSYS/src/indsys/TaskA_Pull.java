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
			long start = System.nanoTime();
			EbookLineReader ebookLineReader = new EbookLineReader(sourceFileName);
			LineBuilder lineBuilder = new LineBuilder(ebookLineReader);
			LineSpinner lineSpinner = new LineSpinner(lineBuilder);
			LineFilter lineFilter = new LineFilter((Readable<Line>)lineSpinner);
			LineSorter lineSorter = new LineSorter((Readable<Line>)lineFilter);
			LineToString lineToString = new LineToString(lineSorter);
			long beforePrint = System.nanoTime();
			ConsoleSink<String> consoleSink = new ConsoleSink<>(lineToString);
			long end = System.nanoTime();
			System.out.println("Took " + (end - start) * 1.0 / 1000 / 1000 / 1000 + " seconds.");
			System.out.println("(" + (beforePrint - start) * 1.0 / 1000 / 1000 / 1000 + " without printing to console)");
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
