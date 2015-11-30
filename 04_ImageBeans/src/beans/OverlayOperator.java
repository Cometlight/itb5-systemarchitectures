package beans;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.logging.Level;

import javax.media.jai.JAI;
import javax.media.jai.operator.BandMergeDescriptor;

import itb5.types.ImageWrapper;

public class OverlayOperator extends PropertySupportBean {
	private static final long serialVersionUID = 1L;

	private ImageWrapper image;
	private ImageWrapper originalImage1, originalImage2;

	public OverlayOperator() {
		image = null;
	}

	/**
	 * @see #setImage(ImageWrapper)
	 * @return the processed image
	 */
	public ImageWrapper getImage() {
		return image;
	}

	/**
	 * Fires property change event "image", if the image given is valid.
	 * 
	 * @see #getImage()
	 * @param newImageWrapper
	 *            the new image to be processed.
	 */
	public void setImage1(ImageWrapper newImageWrapper) {
		originalImage1 = newImageWrapper != null ? newImageWrapper.clone() : null;

		process();
	}

	public void setImage2(ImageWrapper newImageWrapper) {
		originalImage2 = newImageWrapper != null ? newImageWrapper.clone() : null;

		process();
	}

	private void process() {
		if (originalImage1 != null && originalImage2 != null) {
			try {
				ImageWrapper oldImage = image;


				ImageWrapper newImage = new ImageWrapper(JAI.create("overlay",
						originalImage1.getImage().getAsBufferedImage(),
						makeImageTranslucent(originalImage2.getImage().getAsBufferedImage(), 0.3d)));

				if (newImage != null) {
					image = newImage.clone();
				}
				pcs.firePropertyChange("image", oldImage, newImage);
			} catch (InvalidParameterException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public static BufferedImage makeImageTranslucent(BufferedImage source, double alpha) {
		BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(),
				java.awt.Transparency.TRANSLUCENT);
		// Get the images graphics
		Graphics2D g = target.createGraphics();
		// Set the Graphics composite to Alpha
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
		// Draw the image into the prepared reciver image
		g.drawImage(source, null, 0, 0);
		// let go of all system resources in this Graphics
		g.dispose();
		// Return the image
		return target;
	}

	// private void process() {
	// if (originalImage1 != null || originalImage2 != null) {
	// try {
	// ImageWrapper oldImage = image;
	// ImageWrapper newImage = new ImageWrapper(null);
	//
	//
	// ParameterBlock pb = new ParameterBlock();
	// pb.add(new Float(originalImage1.getImage().getWidth()))
	// .add(new Float(originalImage1.getImage().getHeight()));
	// pb.add(new Byte[] {new Byte((byte)0xFF)});
	// RenderedImage alpha = JAI.create("constant", pb);
	//
	// newImage.setImage(BandMergeDescriptor.create(originalImage1.getImage(),
	// alpha, null));
	//
	// if (newImage != null) {
	// image = newImage.clone();
	// }
	// pcs.firePropertyChange("image", oldImage, newImage);
	// } catch (InvalidParameterException e) {
	// log.log(Level.SEVERE, e.getMessage(), e);
	// }
	// }
	// }
}
