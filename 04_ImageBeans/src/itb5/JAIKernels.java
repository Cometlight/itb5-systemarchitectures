package itb5;

import javax.media.jai.KernelJAI;

public class JAIKernels {
	public static final KernelJAI circle7 = new KernelJAI(7, 7, new float[] {
			0, 0, 0, 0, 0, 0, 0,
			0, 0, 1, 1, 1, 0, 0,
			0, 1, 1, 1, 1, 1, 0,
			0, 1, 1, 1, 1, 1, 0,
			0, 1, 1, 1, 1, 1, 0,
			0, 0, 1, 1, 1, 0, 0,
			0, 0, 0, 0, 0, 0, 0
	});
	
	public static final KernelJAI circle5 = new KernelJAI(5, 5, new float[] {
			0, 0, 0, 0, 0,
			0, 0, 1, 0, 0,
			0, 1, 1, 1, 0,
			0, 0, 1, 0, 0,
			0, 0, 0, 0, 0
	});
}
