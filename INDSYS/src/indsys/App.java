package indsys;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;

import indsys.filter.EbookReader;
import indsys.filter.LineBuilder;
import indsys.filter.LineFilter;
import indsys.filter.LineSorter;
import indsys.filter.LineSpinner;
import indsys.filter.LineToString;
import indsys.types.Line;
import pimpmypipe.interfaces.Writeable;

public class App {

	public static void main(String[] args) {
		try {
			EbookReader ebookReader = new EbookReader("aliceInWonderland_short.txt");
			LineBuilder lineBuilder = new LineBuilder(ebookReader, new Writeable<Line>() {

				@Override
				public void write(Line value) throws StreamCorruptedException {
					// TODO Auto-generated method stub
					
				}
			});
			LineSpinner lineSpinner = new LineSpinner(lineBuilder, new Writeable<Line>() {

				@Override
				public void write(Line value) throws StreamCorruptedException {
					// TODO Auto-generated method stub
					
				}
			});
			LineSorter lineSorter = new LineSorter(lineSpinner, new Writeable<Line>() {
				@Override
				public void write(Line value) throws StreamCorruptedException {
					// TODO Auto-generated method stub
					
				}
			});
			LineFilter lineFilter = new LineFilter(lineSorter, new Writeable<Line>() {

				@Override
				public void write(Line value) throws StreamCorruptedException {
					// TODO Auto-generated method stub
					
				}
			});
			LineToString lineToString = new LineToString(lineFilter, new Writeable<String>() {

				@Override
				public void write(String value) throws StreamCorruptedException {
					// TODO Auto-generated method stub
					
				}
			});
			String line;
			while( (line = lineToString.read()) != null) {
				System.out.println(line);
			}
					
					
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
