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
	
	public static void main(String[] args) throws StreamCorruptedException {
		if(args.length < 1) {
			System.out.println("Please provide the desired mode as the first parameter. (\"pull\", \"push\", or \"multithreading\")");
			return;
		}
		String mode = args[0].toLowerCase();
		if(mode.equals("pull")) {
			pull();
		} else if (mode.equals("push")) {
			push();
		} else if (mode.equals("multithreading")) {
			multithreading();
		} else {
			System.out.println("Invalid mode: " + mode + "\nPossible modes: \"pull\", \"push\", or \"multithreading\"");
		}
	}
	
	private static void pull() throws StreamCorruptedException {
		Timer timer = new Timer();
		timer.start();
		
		ImageFileSource imageFileSource = new ImageFileSource(DEFAULT_FILE_PATH, 1000);	// passive source
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
		AbstractSink<LinkedList<Coordinate>> sink = new AbstractSink<>(coordinateConverter);	// active sink
		
		timer.stop();
		System.out.println(timer.getTotalTimeInSeconds());
	}
	
	private static void push() throws StreamCorruptedException {
		Timer timer = new Timer();
		timer.start();
		
		AbstractSink<LinkedList<Coordinate>> sink = new AbstractSink<>();	// passive sink
		AbsoluteCoordinatesConverter coordinateConverter = new AbsoluteCoordinatesConverter(new Coordinate(RECTANGLE.x, RECTANGLE.y), (Writeable<LinkedList<Coordinate>>)sink);
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
		ImageFileSource imageFileSource = new ImageFileSource(DEFAULT_FILE_PATH, 100, imageSaver01);	// active source
		
		timer.stop();
		System.out.println(timer.getTotalTimeInSeconds());
	}
	
	private static void multithreading() throws StreamCorruptedException {
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
		
		Timer timer = new Timer();
		timer.start();
		new Thread(() -> {
			try {
				new AbstractSink<>(pipe13);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
			timer.stop();
			System.out.println(timer.getTotalTimeInSeconds());
		}, "sink").start();
		new Thread(new AbsoluteCoordinatesConverter(new Coordinate(RECTANGLE.x, RECTANGLE.y), pipe12, (Writeable<LinkedList<Coordinate>>)pipe13), "coordinate converter").start();
		new Thread(new CalcCentroidsFilter((Readable<PlanarImage>)pipe11, (Writeable<LinkedList<Coordinate>>)pipe12), "calcCentroidsFilter").start();
		new Thread(new ImageWrapperToPlanarImageConverter(pipe10, pipe11), "imageWrapperToPlanarImageConverter").start();
		new Thread(new ImageSaver("step05", pipe09, (Writeable<ImageWrapper>)pipe10), "ImageSaver 05").start();
		new Thread(new OpeningOperator(JAIKernels.circle7, 2, pipe08, (Writeable<ImageWrapper>)pipe09), "OpeningOperator").start();
		new Thread(new ImageSaver("step04", pipe07, (Writeable<ImageWrapper>)pipe08), "ImageSaver 04").start();
		new Thread(new MedianOperator(pipe06, (Writeable<ImageWrapper>)pipe07), "MedianOperator").start();
		new Thread(new ImageSaver("step03", pipe05, (Writeable<ImageWrapper>)pipe06), "ImageSaver 03").start();
		new Thread(new ThresholdOperator(THRESHOLD_LOW, THRESHOLD_HIGH, THRESHOLD_MAP, pipe04, (Writeable<ImageWrapper>)pipe05), "ThresholdOperator").start();
		new Thread(new ImageSaver("step02", pipe03, (Writeable<ImageWrapper>)pipe04), "ImageSaver 02").start();
		new Thread(new ImageCropper(RECTANGLE, pipe02, (Writeable<ImageWrapper>)pipe03), "ImageCropper").start();
		new Thread(new ImageSaver("step01", pipe01, (Writeable<ImageWrapper>)pipe02), "ImageSaver 01").start();
		new Thread(() -> new ImageFileSource(DEFAULT_FILE_PATH, 100, pipe01), "ImageFileSource").start();
	}
}
