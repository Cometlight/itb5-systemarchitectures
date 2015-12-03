package beans;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.logging.Level;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.operator.BandMergeDescriptor;
import javax.media.jai.operator.CompositeDescriptor;

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


//				ImageWrapper newImage = new ImageWrapper(JAI.create("overlay",
//						originalImage1.getImage().getAsBufferedImage(),
//						makeImageTranslucent(originalImage2.getImage().getAsBufferedImage(), 0.3d)));
				
				// working, but without transparency:
//				ImageWrapper newImage = new ImageWrapper(JAI.create("overlay",
//						originalImage1.getImage().getAsBufferedImage(),
//						originalImage2.getImage().getAsBufferedImage()));
				
				// make image 2 transparent
//				ImageWrapper newImage = new ImageWrapper(PlanarImage.wrapRenderedImage(makeImageTranslucent(originalImage2.getImage().getAsBufferedImage(), 0.5d)));

//				ImageWrapper newImage = new ImageWrapper(JAI.create("overlay",
//						toBufferedImage(TransformColorToTransparency(originalImage1.getImage().getAsBufferedImage(), new Color(0, 0, 0), new Color(1, 1, 1)   )),
//						toBufferedImage(TransformColorToTransparency(originalImage2.getImage().getAsBufferedImage(), new Color(0, 0, 0), new Color(150, 150, 150)   ))));
//				ImageWrapper newImage = new ImageWrapper(PlanarImage.wrapRenderedImage(toBufferedImage(TransformColorToTransparency(originalImage1.getImage().getAsBufferedImage(), new Color(0, 0, 0), new Color(200, 200, 200)))));
				
//				ImageWrapper newImage = new ImageWrapper(JAI.create("overlay",
//						makeImageTranslucent(originalImage1.getImage().getAsBufferedImage(), 0.5d),
//						makeImageTranslucent(originalImage2.getImage().getAsBufferedImage(), 0.5d)   ));
				
//				ParameterBlock pb = new ParameterBlock();
//				pb.addSource(originalImage1.getImage());
//				pb.addSource(originalImage2.getImage());
//				pb.add(new Boolean(false));
//				pb.add(CompositeDescriptor.DESTINATION_ALPHA_LAST);
//				ImageWrapper newImage = new ImageWrapper(JAI.create("composite",
//						originalImage1.getImage(),
//						originalImage2.getImage(),
//						false,
//						CompositeDescriptor.NO_DESTINATION_ALPHA));
				
//				ImageWrapper newImage = new ImageWrapper(JAI.create("composite", pb));
				
				
//				ParameterBlock pb = new ParameterBlock();
//				pb.add(originalImage1.getImage());
//				RenderedImage src1 = (RenderedImage)JAI.create("stream", pb);
//				
//				pb = new ParameterBlock();
//				pb.add(originalImage2.getImage());
//				RenderedImage src2 = (RenderedImage)JAI.create("stream", pb);
				
				ParameterBlock pb = new ParameterBlock();
				pb.addSource(originalImage1.getImage());
				pb.addSource(originalImage2.getImage());
				RenderedImage dst = (RenderedImage)JAI.create("add", pb);
				
				ImageWrapper newImage = new ImageWrapper(PlanarImage.wrapRenderedImage(dst));
				
				
				if (newImage != null) {
					image = newImage.clone();
				}
				pcs.firePropertyChange("image", oldImage, newImage);
			} catch (InvalidParameterException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	 private Image TransformColorToTransparency(BufferedImage image, Color c1, Color c2)
	  {
	    // Primitive test, just an example
	    final int r1 = c1.getRed();
	    final int g1 = c1.getGreen();
	    final int b1 = c1.getBlue();
	    final int r2 = c2.getRed();
	    final int g2 = c2.getGreen();
	    final int b2 = c2.getBlue();
	    ImageFilter filter = new RGBImageFilter()
	    {
	      public final int filterRGB(int x, int y, int rgb)
	      {
	        int r = (rgb & 0xFF0000) >> 16;
	        int g = (rgb & 0xFF00) >> 8;
	        int b = rgb & 0xFF;
	        if (r >= r1 && r <= r2 &&
	            g >= g1 && g <= g2 &&
	            b >= b1 && b <= b2)
	        {
	          // Set fully transparent but keep color
	          return rgb & 0xFFFFFF;
	        }
	        return rgb;
	      }
	    };

	    ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	      return Toolkit.getDefaultToolkit().createImage(ip);
	  }


//	public static BufferedImage makeImageTranslucent(BufferedImage source, double alpha) {
//		BufferedImage target = new BufferedImage(source.getWidth(), source.getHeight(),
//				java.awt.Transparency.TRANSLUCENT);
//		// Get the images graphics
//		Graphics2D g = target.createGraphics();
//		// Set the Graphics composite to Alpha
//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
//		// Draw the image into the prepared reciver image
//		g.drawImage(source, null, 0, 0);
//		// let go of all system resources in this Graphics
//		g.dispose();
//		// Return the image
//		return target;
//	}
	public static BufferedImage makeImageTranslucent(BufferedImage source, double alpha) {
		ParameterBlock pb = new ParameterBlock();
		pb.add(new Float(source.getWidth())).add(new Float(source.getHeight()));
		pb.add(new Byte[] {new Byte((byte)0xFF)});
		RenderedImage newimg = JAI.create("constant", pb);
	
		source = PlanarImage.wrapRenderedImage(BandMergeDescriptor.create(source, newimg, null)).getAsBufferedImage();
		
		return source;
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
