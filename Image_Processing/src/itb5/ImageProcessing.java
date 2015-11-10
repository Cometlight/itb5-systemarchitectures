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
import itb5.filter.OpeningOperator;
import itb5.filter.ThresholdOperator;
import itb5.types.Coordinate;
import itb5.types.ImageWrapper;
import pimpmypipe.interfaces.Readable;

public class ImageProcessing {
	public static final String DEFAULT_FILE_PATH = "loetstellen.jpg";
	public static final Rectangle RECTANGLE = new Rectangle(0, 40, 450, 140);
	public static final int THRESHOLD_LOW = 0;
	public static final int THRESHOLD_HIGH = 30;
	public static final int THRESHOLD_MAP = 255;
//	public static final KernelJAI KERNEL_OPENING = new KernelJAI(3, 3, new float[] { 1, 0, 1,
//																					 0, 1, 0,
//																					 1, 0, 1});
//	public static final KernelJAI KERNEL_OPENING = new KernelJAI(5, 5, new float[] { 1, 0, 0, 0, 1,
//																					 0, 1, 0, 1, 0,
//																					 0, 0, 1, 0, 0,
//																					 0, 1, 0, 1, 0,
////																					 1, 0, 0, 0, 1});
//	public static final KernelJAI KERNEL_OPENING = new KernelJAI(7, 7, new float[] { 1, 0, 0, 0, 0, 0, 1,
//																					 0, 1, 0, 0, 0, 1, 0,
//																					 0, 0, 1, 0, 1, 0, 0,
//																					 0, 0, 0, 1, 0, 0, 0,
//																					 0, 0, 1, 0, 1, 0, 0,
//																					 0, 1, 0, 0, 0, 1, 0,
//																					 1, 0, 0, 0, 0, 0, 1});
	// TODO evtl. Medianfilter vor Opening
	public static void main(String[] args) throws StreamCorruptedException {
		ImageFileSource imageFileSource = new ImageFileSource(DEFAULT_FILE_PATH, 1);
		ImageSaver imageSaver01 = new ImageSaver("step01", imageFileSource);
		ImageCropper imageCropper = new ImageCropper(RECTANGLE, (Readable<ImageWrapper>)imageSaver01);
		ImageSaver imageSaver02 = new ImageSaver("step02", (Readable<ImageWrapper>)imageCropper);
		ThresholdOperator thresholdOperator = new ThresholdOperator(THRESHOLD_LOW, THRESHOLD_HIGH, THRESHOLD_MAP, (Readable<ImageWrapper>)imageSaver02);
		ImageSaver imageSaver03 = new ImageSaver("step03", (Readable<ImageWrapper>)thresholdOperator);
		OpeningOperator openingOperator = new OpeningOperator(JAIKernels.circle7, 2, (Readable<ImageWrapper>)imageSaver03);
		ImageSaver imageSaver04 = new ImageSaver("step04", (Readable<ImageWrapper>)openingOperator);
		ImageWrapperToPlanarImageConverter imgConverter = new ImageWrapperToPlanarImageConverter(imageSaver04);
		CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(imgConverter);
		AbstractSink<LinkedList<Coordinate>> sink = new AbstractSink<>(calcCentroidsFilter);
	}
}
