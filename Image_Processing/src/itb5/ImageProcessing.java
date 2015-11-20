package itb5;

import java.awt.Rectangle;
import java.io.StreamCorruptedException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import itb5.filter.AbsoluteCoordinatesConverter;
import itb5.filter.AbstractSink;
import itb5.filter.CalcCentroidsFilter;
import itb5.filter.ImageCropper;
import itb5.filter.ImageFileSource;
import itb5.filter.ImageSaver;
import itb5.filter.ImageWrapperToPlanarImageConverter;
import itb5.filter.MedianOperator;
import itb5.filter.OpeningOperator;
import itb5.filter.ThresholdOperator;
import itb5.filter.ToleranceChecker;
import itb5.types.Coordinate;
import itb5.types.ImageWrapper;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;
import pimpmypipe.pipes.SynchronizedPipe;

public class ImageProcessing {
	private static final Logger _log = Logger.getLogger(ImageProcessing.class.getName());
	
	public static final String DEFAULT_FILE_PATH = "loetstellen.jpg";
	public static final Rectangle RECTANGLE = new Rectangle(40, 40, 408, 140);	// Lötstelle ganz links wird abgeschnitten, da sie nicht vollständig vorhanden ist
	public static final int THRESHOLD_LOW = 0;
	public static final int THRESHOLD_HIGH = 30;
	public static final int THRESHOLD_MAP = 255;
	public static final String EXPECTED_COORDINATES = "[(73,77), (110,80), (202,80), (265,79), (330,81), (396,81)]";
	public static final int TOLERANCE = 10;
	public static final String OUTPUT_FILE_NAME = "result.txt";
	
	public static void main(String[] args) throws StreamCorruptedException {
		if(args.length < 2) {
			System.out.println("Please provide the desired mode as the first parameter. (\"pull\", \"push\", or \"multithreading\")\n"
					+ "Please provide a number indicating how often the image shall be processed as the second parameter.");
			return;
		}
		String mode = args[0].toLowerCase();
		int nrOfTimes = Integer.valueOf(args[1]);
		String inputFileName = args[2];
		String outputFileName = args[3];
		String expectedCoordinates = args[4];
		int tolerance = Integer.valueOf(args[5]);
		Coordinate upperLeft = CoordinateUtility.toCoordinate(args[6]);
		Coordinate size = CoordinateUtility.toCoordinate(args[7]);
		
		if(nrOfTimes <= 0) {
			System.out.println("Please provide a number > 0");
		}
		
		if(mode.equals("pull")) {
			pull(nrOfTimes);
		} else if (mode.equals("push")) {
			push(nrOfTimes);
		} else if (mode.equals("multithreading")) {
			multithreading(nrOfTimes);
		} else {
			System.out.println("Invalid mode: " + mode + "\nPossible modes: \"pull\", \"push\", or \"multithreading\"");
		}
	}
	
	private static void pull(int nrOfTimes) throws StreamCorruptedException {
		Timer timer = new Timer();
		timer.start();
		
		ImageFileSource imageFileSource = new ImageFileSource(DEFAULT_FILE_PATH, nrOfTimes);	// passive source
		ImageSaver imageSaver01 = new ImageSaver("step01", imageFileSource);
		ImageCropper imageCropper = new ImageCropper(RECTANGLE, (Readable<ImageWrapper>)imageSaver01);
		ImageSaver imageSaver02 = new ImageSaver("step02", (Readable<ImageWrapper>)imageCropper);
		ThresholdOperator thresholdOperator = new ThresholdOperator(THRESHOLD_LOW, THRESHOLD_HIGH, THRESHOLD_MAP, (Readable<ImageWrapper>)imageSaver02);
		ImageSaver imageSaver03 = new ImageSaver("step03", (Readable<ImageWrapper>)thresholdOperator);
		MedianOperator medianOperator = new MedianOperator((Readable<ImageWrapper>)imageSaver03);
		ImageSaver imageSaver04 = new ImageSaver("step04", (Readable<ImageWrapper>)medianOperator);
		OpeningOperator openingOperator = new OpeningOperator(JAIKernels.circle7, 2, (Readable<ImageWrapper>)imageSaver04);
		ImageSaver imageSaver05 = new ImageSaver("step05", (Readable<ImageWrapper>)openingOperator);
		ImageWrapperToPlanarImageConverter imgConverter = new ImageWrapperToPlanarImageConverter(imageSaver05);
		CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(imgConverter);
		AbsoluteCoordinatesConverter coordinateConverter = new AbsoluteCoordinatesConverter(new Coordinate(RECTANGLE.x, RECTANGLE.y), (Readable<LinkedList<Coordinate>>)calcCentroidsFilter);
		ToleranceChecker toleranceChecker = new ToleranceChecker(CoordinateUtility.toCoordinateList(EXPECTED_COORDINATES), TOLERANCE, coordinateConverter);
		AbstractSink<LinkedList<Boolean>> sink = new AbstractSink<>(OUTPUT_FILE_NAME, toleranceChecker);	// active sink
		
		timer.stop();
		System.out.println(timer.getTotalTimeInSeconds());
	}
	
	private static void push(int nrOfTimes) throws StreamCorruptedException {
		Timer timer = new Timer();
		timer.start();
		
		AbstractSink<LinkedList<Boolean>> sink = new AbstractSink<>(OUTPUT_FILE_NAME);	// passive sink
		ToleranceChecker toleranceChecker = new ToleranceChecker(CoordinateUtility.toCoordinateList(EXPECTED_COORDINATES), TOLERANCE, (Writeable<LinkedList<Boolean>>)sink);
		AbsoluteCoordinatesConverter coordinateConverter = new AbsoluteCoordinatesConverter(new Coordinate(RECTANGLE.x, RECTANGLE.y), (Writeable<LinkedList<Coordinate>>)toleranceChecker);
		CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(coordinateConverter);
		ImageWrapperToPlanarImageConverter imgConverter = new ImageWrapperToPlanarImageConverter(calcCentroidsFilter);
		ImageSaver imageSaver05 = new ImageSaver("step05", (Writeable<ImageWrapper>)imgConverter);
		OpeningOperator openingOperator = new OpeningOperator(JAIKernels.circle7, 2, (Writeable<ImageWrapper>)imageSaver05);
		ImageSaver imageSaver04 = new ImageSaver("step04", (Writeable<ImageWrapper>)openingOperator);
		MedianOperator medianOperator = new MedianOperator((Writeable<ImageWrapper>)imageSaver04);
		ImageSaver imageSaver03 = new ImageSaver("step03", (Writeable<ImageWrapper>)medianOperator);
		ThresholdOperator thresholdOperator = new ThresholdOperator(THRESHOLD_LOW, THRESHOLD_HIGH, THRESHOLD_MAP, (Writeable<ImageWrapper>)imageSaver03);
		ImageSaver imageSaver02 = new ImageSaver("step02", (Writeable<ImageWrapper>)thresholdOperator);
		ImageCropper imageCropper = new ImageCropper(RECTANGLE, (Writeable<ImageWrapper>)imageSaver02);
		ImageSaver imageSaver01 = new ImageSaver("step01", (Writeable<ImageWrapper>)imageCropper);
		ImageFileSource imageFileSource = new ImageFileSource(DEFAULT_FILE_PATH, nrOfTimes, imageSaver01);	// active source
		
		timer.stop();
		System.out.println(timer.getTotalTimeInSeconds());
	}
	
	private static void multithreading(int nrOfTimes) throws StreamCorruptedException {
		SynchronizedPipe<ImageWrapper> pipe01 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe02 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe03 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe04 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe05 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe06 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe07 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe08 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe09 = new SynchronizedPipe<>();
		SynchronizedPipe<ImageWrapper> pipe10 = new SynchronizedPipe<>();
		SynchronizedPipe<PlanarImage> pipe11 = new SynchronizedPipe<>();
		SynchronizedPipe<LinkedList<Coordinate>> pipe12 = new SynchronizedPipe<>();
		SynchronizedPipe<LinkedList<Coordinate>> pipe13 = new SynchronizedPipe<>();
		SynchronizedPipe<LinkedList<Boolean>> pipe14 = new SynchronizedPipe<>();
		
		Timer timer = new Timer();
		timer.start();
		new Thread(() -> {
			try {
				new AbstractSink<>(OUTPUT_FILE_NAME, pipe14);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
			timer.stop();
			System.out.println(timer.getTotalTimeInSeconds());
		}, "sink").start();
		new Thread(new ToleranceChecker(CoordinateUtility.toCoordinateList(EXPECTED_COORDINATES), TOLERANCE, pipe13, pipe14)).start();
		new Thread(new AbsoluteCoordinatesConverter(new Coordinate(RECTANGLE.x, RECTANGLE.y), pipe12, pipe13), "coordinate converter").start();
		new Thread(new CalcCentroidsFilter((Readable<PlanarImage>)pipe11, pipe12), "calcCentroidsFilter").start();
		new Thread(new ImageWrapperToPlanarImageConverter(pipe10, pipe11), "imageWrapperToPlanarImageConverter").start();
		new Thread(new ImageSaver("step05", pipe09, pipe10), "ImageSaver 05").start();
		new Thread(new OpeningOperator(JAIKernels.circle7, 2, pipe08, pipe09), "OpeningOperator").start();
		new Thread(new ImageSaver("step04", pipe07, pipe08), "ImageSaver 04").start();
		new Thread(new MedianOperator(pipe06, pipe07), "MedianOperator").start();
		new Thread(new ImageSaver("step03", pipe05, pipe06), "ImageSaver 03").start();
		new Thread(new ThresholdOperator(THRESHOLD_LOW, THRESHOLD_HIGH, THRESHOLD_MAP, pipe04, pipe05), "ThresholdOperator").start();
		new Thread(new ImageSaver("step02", pipe03, pipe04), "ImageSaver 02").start();
		new Thread(new ImageCropper(RECTANGLE, pipe02, pipe03), "ImageCropper").start();
		new Thread(new ImageSaver("step01", pipe01, pipe02), "ImageSaver 01").start();
		new Thread(() -> new ImageFileSource(DEFAULT_FILE_PATH, nrOfTimes, pipe01), "ImageFileSource").start();
	}
}
