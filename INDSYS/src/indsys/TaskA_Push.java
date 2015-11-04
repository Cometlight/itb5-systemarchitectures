package indsys;

import java.io.File;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import indsys.filter.EbookLineReader;
import indsys.filter.LineBuilder;
import indsys.filter.LineFilter;
import indsys.filter.LineSorter;
import indsys.filter.LineSpinner;
import indsys.filter.LineToString;
import indsys.types.Line;
import pimpmypipe.interfaces.Writeable;

public class TaskA_Push {
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
			LineToString lineToString = new LineToString(new Writeable<String>() {
				@Override
				public void write(String value) throws StreamCorruptedException {
					System.out.println(value);
				}
			});
			LineSorter lineSorter = new LineSorter(lineToString);
			LineFilter lineFilter = new LineFilter((Writeable<Line>) lineSorter);
			LineSpinner lineSpinner = new LineSpinner((Writeable<Line>) lineFilter);
			LineBuilder lineBuilder = new LineBuilder(lineSpinner);
			EbookLineReader ebookLineReader = new EbookLineReader(sourceFileName, lineBuilder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
