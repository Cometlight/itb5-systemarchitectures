package itb5;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import itb5.filter.AbsoluteCoordinatesConverter;
import itb5.filter.FileSink;
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

/**
 * Use the property file to adjust the settings.
 * Note the possible modes: pull, push, and multithreading
 */
public class ImageProcessing {
	private static final Logger _log = Logger.getLogger(ImageProcessing.class.getName());

	public static final String PROPERTY_FILE_NAME = "settings.prop";

	public static void main(String[] args) throws StreamCorruptedException {
		Settings settings = loadSettingsFromPropertyFile(PROPERTY_FILE_NAME);

		if (settings.getMode().equals("pull")) {
			pull(settings);
		} else if (settings.getMode().equals("push")) {
			push(settings);
		} else if (settings.getMode().equals("multithreading")) {
			multithreading(settings);
		} else {
			System.out.println("Invalid mode: " + settings.getMode() + "\nPossible modes: \"pull\", \"push\", or \"multithreading\"");
		}
	}

	private static void pull(Settings settings) throws StreamCorruptedException {
		ImageFileSource imageFileSource = new ImageFileSource(settings.getInputFileName(), settings.getNumberImageToProcess()); // passive source
		ImageSaver imageSaver01 = new ImageSaver("step01", imageFileSource);
		ImageCropper imageCropper = new ImageCropper(settings.getRegionOfInterest(), (Readable<ImageWrapper>) imageSaver01);
		ImageSaver imageSaver02 = new ImageSaver("step02", (Readable<ImageWrapper>) imageCropper);
		ThresholdOperator thresholdOperator = new ThresholdOperator(settings.getThresholdLow(), settings.getThresholdHigh(), settings.getThresholdMap(),
				(Readable<ImageWrapper>) imageSaver02);
		ImageSaver imageSaver03 = new ImageSaver("step03", (Readable<ImageWrapper>) thresholdOperator);
		MedianOperator medianOperator = new MedianOperator((Readable<ImageWrapper>) imageSaver03);
		ImageSaver imageSaver04 = new ImageSaver("step04", (Readable<ImageWrapper>) medianOperator);
		OpeningOperator openingOperator = new OpeningOperator(JAIKernels.circle7, 2,
				(Readable<ImageWrapper>) imageSaver04);
		ImageSaver imageSaver05 = new ImageSaver("step05", (Readable<ImageWrapper>) openingOperator);
		ImageWrapperToPlanarImageConverter imgConverter = new ImageWrapperToPlanarImageConverter(imageSaver05);
		CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(imgConverter);
		AbsoluteCoordinatesConverter coordinateConverter = new AbsoluteCoordinatesConverter(
				new Coordinate(settings.getRegionOfInterest().x, settings.getRegionOfInterest().y), (Readable<LinkedList<Coordinate>>) calcCentroidsFilter);
		ToleranceChecker toleranceChecker = new ToleranceChecker(settings.getExpectedCoordinates(), settings.getTolerance(), coordinateConverter);
		FileSink<LinkedList<Boolean>> sink = new FileSink<>(settings.getOutputFileName(), toleranceChecker); // active sink
		
		Timer timer = new Timer();
		timer.start();
		
		sink.readAll();

		timer.stop();
		System.out.println(timer.getTotalTimeInSeconds());
	}

	private static void push(Settings settings) throws StreamCorruptedException {
		FileSink<LinkedList<Boolean>> sink = new FileSink<>(settings._outputFileName); // passive sink
		ToleranceChecker toleranceChecker = new ToleranceChecker(settings.getExpectedCoordinates(), settings.getTolerance(), (Writeable<LinkedList<Boolean>>) sink);
		AbsoluteCoordinatesConverter coordinateConverter = new AbsoluteCoordinatesConverter(
				new Coordinate(settings.getRegionOfInterest().x, settings.getRegionOfInterest().y), (Writeable<LinkedList<Coordinate>>) toleranceChecker);
		CalcCentroidsFilter calcCentroidsFilter = new CalcCentroidsFilter(coordinateConverter);
		ImageWrapperToPlanarImageConverter imgConverter = new ImageWrapperToPlanarImageConverter(calcCentroidsFilter);
		ImageSaver imageSaver05 = new ImageSaver("step05", (Writeable<ImageWrapper>) imgConverter);
		OpeningOperator openingOperator = new OpeningOperator(JAIKernels.circle7, 2,
				(Writeable<ImageWrapper>) imageSaver05);
		ImageSaver imageSaver04 = new ImageSaver("step04", (Writeable<ImageWrapper>) openingOperator);
		MedianOperator medianOperator = new MedianOperator((Writeable<ImageWrapper>) imageSaver04);
		ImageSaver imageSaver03 = new ImageSaver("step03", (Writeable<ImageWrapper>) medianOperator);
		ThresholdOperator thresholdOperator = new ThresholdOperator(settings.getThresholdLow(), settings.getThresholdHigh(), settings.getThresholdMap(),
				(Writeable<ImageWrapper>) imageSaver03);
		ImageSaver imageSaver02 = new ImageSaver("step02", (Writeable<ImageWrapper>) thresholdOperator);
		ImageCropper imageCropper = new ImageCropper(settings.getRegionOfInterest(), (Writeable<ImageWrapper>) imageSaver02);
		ImageSaver imageSaver01 = new ImageSaver("step01", (Writeable<ImageWrapper>) imageCropper);
		ImageFileSource imageFileSource = new ImageFileSource(settings.getInputFileName(), settings.getNumberImageToProcess(), imageSaver01); // active source
		
		Timer timer = new Timer();
		timer.start();
		
		imageFileSource.writeAll();

		timer.stop();
		System.out.println(timer.getTotalTimeInSeconds());
	}

	private static void multithreading(Settings settings) throws StreamCorruptedException {
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
				new FileSink<>(settings.getOutputFileName(), pipe14).readAll();;
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
			timer.stop();
			System.out.println(timer.getTotalTimeInSeconds());
		} , "sink").start();
		new Thread(new ToleranceChecker(settings.getExpectedCoordinates(), settings.getTolerance(), pipe13, pipe14)).start();
		new Thread(new AbsoluteCoordinatesConverter(new Coordinate(settings.getRegionOfInterest().x, settings.getRegionOfInterest().y), pipe12, pipe13),
				"coordinate converter").start();
		new Thread(new CalcCentroidsFilter((Readable<PlanarImage>) pipe11, pipe12), "calcCentroidsFilter").start();
		new Thread(new ImageWrapperToPlanarImageConverter(pipe10, pipe11), "imageWrapperToPlanarImageConverter")
				.start();
		new Thread(new ImageSaver("step05", pipe09, pipe10), "ImageSaver 05").start();
		new Thread(new OpeningOperator(JAIKernels.circle7, 2, pipe08, pipe09), "OpeningOperator").start();
		new Thread(new ImageSaver("step04", pipe07, pipe08), "ImageSaver 04").start();
		new Thread(new MedianOperator(pipe06, pipe07), "MedianOperator").start();
		new Thread(new ImageSaver("step03", pipe05, pipe06), "ImageSaver 03").start();
		new Thread(new ThresholdOperator(settings.getThresholdLow(), settings.getThresholdHigh(), settings.getThresholdMap(), pipe04, pipe05),
				"ThresholdOperator").start();
		new Thread(new ImageSaver("step02", pipe03, pipe04), "ImageSaver 02").start();
		new Thread(new ImageCropper(settings.getRegionOfInterest(), pipe02, pipe03), "ImageCropper").start();
		new Thread(new ImageSaver("step01", pipe01, pipe02), "ImageSaver 01").start();
		new Thread(() -> new ImageFileSource(settings.getInputFileName(), settings.getNumberImageToProcess(), pipe01).writeAll(), "ImageFileSource").start();
	}

	private static Settings loadSettingsFromPropertyFile(String filePath) {
		if (!new File(filePath).exists()) {
			createDefaultPropertyFile(filePath);
		}
		
		Settings settings = null;
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream(filePath)) {
			prop.load(input);
			settings = new Settings(
					prop.getProperty("input_file_name"),
					prop.getProperty("output_file_name"),
					prop.getProperty("mode"),
					Integer.valueOf(prop.getProperty("number_image_to_process")),
					CoordinateUtility.toCoordinateList(prop.getProperty("expected_coordinates")),
					Integer.valueOf(prop.getProperty("tolerance")),
					CoordinateUtility.toCoordinate(prop.getProperty("region_of_interest_upper_left")),
					CoordinateUtility.toCoordinate(prop.getProperty("region_of_interest_width_height")),
					Integer.valueOf(prop.getProperty("threshold_low")),
					Integer.valueOf(prop.getProperty("threshold_high")),
					Integer.valueOf(prop.getProperty("threshold_map"))
				);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		return settings;
	}

	private static void createDefaultPropertyFile(String filePath) {
		Properties prop = new Properties();
		prop.setProperty("input_file_name", "loetstellen.jpg");
		prop.setProperty("output_file_name", "result.txt");
		prop.setProperty("mode", "multithreading");
		prop.setProperty("number_image_to_process", "50");
		prop.setProperty("expected_coordinates", "[(73,77), (110,80), (202,80), (265,79), (330,81), (396,81)]");
		prop.setProperty("tolerance", "10");
		prop.setProperty("region_of_interest_upper_left", "(40,40)");
		prop.setProperty("region_of_interest_width_height", "(408,140)");
		prop.setProperty("threshold_low", "0");
		prop.setProperty("threshold_high", "30");
		prop.setProperty("threshold_map", "255");
		try (OutputStream output = new FileOutputStream(filePath)) {
			prop.store(output, null);
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	private static class Settings {
		private String _inputFileName;
		private String _outputFileName;
		private String _mode;
		private int _numberImageToProcess;
		private LinkedList<Coordinate> _expectedCoordinates;
		private int _tolerance;
		private Coordinate _roiUpperLeft;
		private Coordinate _roiWidthHeight;
		private int _thresholdLow;
		private int _thresholdHigh;
		private int _thresholdMap;
		
		public Settings(String _inputFileName, String _outputFileName, String _mode, int _numberImageToProcess,
				LinkedList<Coordinate> _expectedCoordinates, int _tolerance, Coordinate _roiUpperLeft,
				Coordinate _roiWidthHeight, int _thresholdLow, int _thresholdHigh, int _thresholdMap) {
			super();
			this._inputFileName = _inputFileName;
			this._outputFileName = _outputFileName;
			this._mode = _mode;
			this._numberImageToProcess = _numberImageToProcess;
			this._expectedCoordinates = _expectedCoordinates;
			this._tolerance = _tolerance;
			this._roiUpperLeft = _roiUpperLeft;
			this._roiWidthHeight = _roiWidthHeight;
			this._thresholdLow = _thresholdLow;
			this._thresholdHigh = _thresholdHigh;
			this._thresholdMap = _thresholdMap;
		}

		public String getInputFileName() {
			return _inputFileName;
		}

		public String getOutputFileName() {
			return _outputFileName;
		}

		public String getMode() {
			return _mode;
		}

		public int getNumberImageToProcess() {
			return _numberImageToProcess;
		}

		public LinkedList<Coordinate> getExpectedCoordinates() {
			return _expectedCoordinates;
		}

		public int getTolerance() {
			return _tolerance;
		}

		public int getThresholdLow() {
			return _thresholdLow;
		}

		public int getThresholdHigh() {
			return _thresholdHigh;
		}

		public int getThresholdMap() {
			return _thresholdMap;
		}

		public Rectangle getRegionOfInterest() {
			return new Rectangle(_roiUpperLeft._x, _roiUpperLeft._y, _roiWidthHeight._x, _roiWidthHeight._y);
		}
	}
}
