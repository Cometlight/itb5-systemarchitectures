package itb5;

import java.awt.Rectangle;
import java.io.StreamCorruptedException;
import java.util.LinkedList;

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
	public static final String DEFAULT_FILE_PATH = "loetstellen.jpg";
	public static final Rectangle RECTANGLE = new Rectangle(40, 40, 408, 140);	// Lötstelle ganz links wird abgeschnitten, da sie nicht vollständig vorhanden ist
	public static final int THRESHOLD_LOW = 0;
	public static final int THRESHOLD_HIGH = 30;
	public static final int THRESHOLD_MAP = 255;
	
	public static void main(String[] args) throws StreamCorruptedException {
		String mode = "pull";	// TODO -> args
		if(mode.equals("pull")) {
			pull();
		} else if (mode.equals("push")) {
			push();
		} else {
			System.out.println("Invalid mode: " + mode);
		}
	}
	
	private static void pull() throws StreamCorruptedException {
		ImageFileSource imageFileSource = new ImageFileSource(DEFAULT_FILE_PATH, 1);	// passive source
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
		AbstractSink<LinkedList<Coordinate>> sink = new AbstractSink<>(calcCentroidsFilter);	// active sink
		// TODO: Calc coordinates in original image
	}
	
	private static void push() throws StreamCorruptedException {
		AbstractSink<LinkedList<Coordinate>> sink = new AbstractSink<>();	// passive sink
		CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(sink);
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
		ImageFileSource imageFileSource = new ImageFileSource(DEFAULT_FILE_PATH, 1, imageSaver01);	// active source
		// TODO: Calc coordinates in original image
	}
	
//	private static void pushMultithreading() throws StreamCorruptedException {
//		AbstractSink<LinkedList<Coordinate>> sink = new AbstractSink<>();	// passive sink
//		CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(sink);
//		ImageWrapperToPlanarImageConverter imgConverter = new ImageWrapperToPlanarImageConverter(calcCentroidsFilter);
//		ImageSaver imageSaver05 = new ImageSaver("step05", (Writeable<ImageWrapper>)imgConverter);
//		OpeningOperator openingOperator = new OpeningOperator(JAIKernels.circle7, 2, (Writeable<ImageWrapper>)imageSaver05);
//		ImageSaver imageSaver04 = new ImageSaver("step04", (Writeable<ImageWrapper>)openingOperator);
//		MedianOperator medianOperator = new MedianOperator((Writeable<ImageWrapper>)imageSaver04);
//		ImageSaver imageSaver03 = new ImageSaver("step03", (Writeable<ImageWrapper>)medianOperator);
//		ThresholdOperator thresholdOperator = new ThresholdOperator(THRESHOLD_LOW, THRESHOLD_HIGH, THRESHOLD_MAP, (Writeable<ImageWrapper>)imageSaver03);
//		ImageSaver imageSaver02 = new ImageSaver("step02", (Writeable<ImageWrapper>)thresholdOperator);
//		ImageCropper imageCropper = new ImageCropper(RECTANGLE, (Writeable<ImageWrapper>)imageSaver02);
//		ImageSaver imageSaver01 = new ImageSaver("step01", (Writeable<ImageWrapper>)imageCropper);
//		SynchronizedPipe<ImageWrapper> pipe01 = new SynchronizedPipe<>();
//		ImageFileSource imageFileSource = new ImageFileSource(DEFAULT_FILE_PATH, 1, imageSaver01);	// active source
//		// TODO: Calc coordinates in original image
//	}
}
