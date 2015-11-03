package indsys;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;

import indsys.filter.AlignedLineBuilder;
import indsys.filter.EbookLineReader;
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
import indsys.types.Word;
import pimpmypipe.filter.SpreadFilter;
import pimpmypipe.interfaces.Writeable;

public class TaskB_Push {
	private static final String DEFAULT_FILE_NAME = "output_TaskA_Push.txt";

	public static void main(String[] args) {
		String fileName;
		if(args.length >= 3) {
			fileName = args[2];
		} else {
			fileName = DEFAULT_FILE_NAME;
		}
		try {
			LineToString lineToString = new LineToString(new Writeable<String>() {
				
				@Override
				public void write(String value) throws StreamCorruptedException {
					System.out.println(value);
					
				}
			});
			
			LineFilter lineFilter = new LineFilter(lineToString);
			
			LineSorter lineSorter = new LineSorter((Writeable)lineFilter);
			
			LineSpinner lineSpinner = new LineSpinner((Writeable)lineSorter);
			
			LineBuilder lineBuilder = new LineBuilder(lineSpinner);
			
			LineFileWriter lineFileWriter = new LineFileWriter("output.txt", lineBuilder);
			AlignedLineBuilder alignedLineBuilder = new AlignedLineBuilder(TextAlignment.Center, 80, lineFileWriter);
			
			WordBuilder wordBuilder = new WordBuilder(alignedLineBuilder);
			EbookReader ebookReader = new EbookReader("aliceInWonderland_short.txt", wordBuilder);
			
					
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
