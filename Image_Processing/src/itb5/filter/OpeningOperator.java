package itb5.filter;

import java.awt.image.renderable.ParameterBlock;
import java.security.InvalidParameterException;

import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.PlanarImage;

import itb5.types.ImageWrapper;
import pimpmypipe.filter.DataTransformationFilter;
import pimpmypipe.interfaces.Readable;
import pimpmypipe.interfaces.Writeable;

public class OpeningOperator  extends DataTransformationFilter<ImageWrapper> {
	private KernelJAI _kernel;
	private int _nrOfProcessingCycles;
	
	public OpeningOperator(KernelJAI kernel, int nrOfProcessingCycles, Readable<ImageWrapper> input, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(input, output);
		init(kernel, nrOfProcessingCycles);
	}

	public OpeningOperator(KernelJAI kernel, int nrOfProcessingCycles, Readable<ImageWrapper> input)
			throws InvalidParameterException {
		super(input);
		init(kernel, nrOfProcessingCycles);
	}
	
	public OpeningOperator(KernelJAI kernel, int nrOfProcessingCycles, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(output);
		init(kernel, nrOfProcessingCycles);
	}
	
	private void init(KernelJAI kernel, int nrOfProcessingCycles) {
		_kernel = kernel;
		_nrOfProcessingCycles = nrOfProcessingCycles;
	}

	@Override
	protected void process(ImageWrapper entity) {
		PlanarImage image = entity.getImage();
		for(int i = 0; i < _nrOfProcessingCycles; ++i) {
			ParameterBlock pb = new ParameterBlock();
			pb.addSource(image);
			pb.add(_kernel);
			image = JAI.create("erode", pb);
		}
		for(int i = 0; i < _nrOfProcessingCycles; ++i) {
			ParameterBlock pb = new ParameterBlock();
			pb.addSource(image);
			pb.add(_kernel);
			image = JAI.create("dilate", pb);
		}
		entity.setImage(image);
	}

}
