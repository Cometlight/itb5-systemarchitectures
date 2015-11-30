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
 * Performs erode, then dilate. Both times using the same kernel.
 * 
 * @note The original opening operator, as used in exercise 02.
 */
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
		entity.setImage(this.openingOperator(entity.getImage(), _nrOfProcessingCycles, _kernel));
	}
	
	/**
	 * Executes the opening operator on the given image multiple times with the
	 * given kernel.
	 * @param image the image to execute the opening operator on
	 * @param cycles number of processing cycles
	 * @param kernel the kernel to use for the operator
	 * @return the processed image
	 */
	private PlanarImage openingOperator(PlanarImage image, int cycles, KernelJAI kernel) {
		for (int i = 0; i < cycles; ++i) {
			image = this.processImage(image, "erode",  kernel);
		}
		for (int i = 0; i < cycles; ++i) {
			image = this.processImage(image, "dilate", kernel);
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
