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

/**
 * Performs "erode" or "dilate", as specified by {@link #_mode}
 */
public class ErodeDilateOperator extends DataTransformationFilter<ImageWrapper> {
	public enum Mode {
		erode, dilate;
	}
	private Mode _mode;
	private KernelJAI _kernel;
	private int _nrOfProcessingCycles;
	
	public ErodeDilateOperator(Mode mode, KernelJAI kernel, int nrOfProcessingCycles, Readable<ImageWrapper> input, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(input, output);
		init(mode, kernel, nrOfProcessingCycles);
	}

	public ErodeDilateOperator(Mode mode, KernelJAI kernel, int nrOfProcessingCycles, Readable<ImageWrapper> input)
			throws InvalidParameterException {
		super(input);
		init(mode, kernel, nrOfProcessingCycles);
	}
	
	public ErodeDilateOperator(Mode mode, KernelJAI kernel, int nrOfProcessingCycles, Writeable<ImageWrapper> output)
			throws InvalidParameterException {
		super(output);
		init(mode, kernel, nrOfProcessingCycles);
	}
	
	private void init(Mode mode, KernelJAI kernel, int nrOfProcessingCycles) {
		_mode = mode;
		_kernel = kernel;
		_nrOfProcessingCycles = nrOfProcessingCycles;
	}

	@Override
	protected void process(ImageWrapper entity) {
		entity.setImage(this.erode(entity.getImage(), _nrOfProcessingCycles, _kernel));
	}
	
	/**
	 * Executes the erode operator on the given image multiple times with the
	 * given kernel.
	 * @param image the image to execute the erode operator on
	 * @param cycles number of processing cycles
	 * @param kernel the kernel to use for the operator
	 * @return the processed image
	 */
	private PlanarImage erode(PlanarImage image, int cycles, KernelJAI kernel) {
		for (int i = 0; i < cycles; ++i) {
			image = this.processImage(image, _mode.toString(),  kernel);
		}
		return image;
	}
	
	/**
	 * Executes an action on the given image with the given kernel
	 * @param image the image to process
	 * @param method the method to perform
	 * @param kernel the kernel to use
	 * @return the processed image
	 */
	private PlanarImage processImage(PlanarImage image, String method, KernelJAI kernel) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(kernel);
		return JAI.create(method, pb);
	}

}
