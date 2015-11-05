package indsys;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import indsys.filter.AlignedLineBuilder;
import indsys.filter.EbookReader;
import indsys.filter.LineBuilder;
import indsys.filter.LineFileWriter;
import indsys.filter.LineFilter;
import indsys.filter.LineSorter;
import indsys.filter.LineSpinner;
import indsys.filter.LineToString;
import indsys.filter.WordBuilder;
import indsys.types.Line;
import indsys.types.TextAlignment;
import pimpmypipe.interfaces.Readable;

public class TaskB_Pull {
	private static final Logger _log = Logger.getLogger(TaskB_Pull.class.getName());
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println(
					"Please provide the file name of the source text as first parameter, the name of the output file as second parameter, the line width as the third parameter, and the alignment [LEFT, CENTER, RIGHT] as the fourth parameter.");
			return;
		}

		String sourceFileName = args[0];

		if (!new File(sourceFileName).exists()) {
			System.out.println(
					"'" + sourceFileName + "' not found. Please provide a valid file name of the source text.");
			return;
		}

		String outputFileName = args[1];
		
		int lineWidth = Integer.valueOf(args[2]);
		
		TextAlignment textAlignment = TextAlignment.valueOf(args[3].toUpperCase());

		try {
			EbookReader ebookReader = new EbookReader(sourceFileName);
			WordBuilder wordBuilder = new WordBuilder(ebookReader);
			AlignedLineBuilder alignedLineBuilder = new AlignedLineBuilder(textAlignment, lineWidth, wordBuilder);
			LineFileWriter lineFileWriter = new LineFileWriter(outputFileName, alignedLineBuilder);
			LineBuilder lineBuilder = new LineBuilder(lineFileWriter);
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
