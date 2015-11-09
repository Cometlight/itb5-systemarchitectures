package itb5.filter;

import java.awt.image.renderable.ParameterBlock;
import java.security.InvalidParameterException;

import javax.media.jai.JAI;

import itb5.types.ImageWrapper;
import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class ThresholdOperator extends DataTransformationFilter<ImageWrapper> {
	private double[] _low;
	private double[] _high;
	private double[] _map;

	public ThresholdOperator(double low, double high, double map, Readable<ImageWrapper> input, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(input, output);
		init(low, high, map);
	}
	
	public ThresholdOperator(double low, double high, double map, Readable<ImageWrapper> input)
			throws InvalidParameterException {
		super(input);
		init(low, high, map);
	}
	
	public ThresholdOperator(double low, double high, double map, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(output);
		init(low, high, map);
	}
	
	private void init(double low, double high, double map) {
		_low = new double[]{low};
		_high = new double[]{high};
		_map = new double[]{map};
	}

	@Override
	protected void process(ImageWrapper entity) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(entity.getImage());
		pb.add(_low);
		pb.add(_high);
		pb.add(_map);
		entity.setImage(JAI.create("threshold", pb));
	}

}
