package beans;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;

import itb5.types.ImageWrapper;

public class ImageCropper2 extends Canvas implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(PropertySupportBean.class.getName());

	private int x;
	private int y;
	private int width;
	private int height;
	private ImageWrapper image;
	private ImageWrapper originalImage;
	private PropertyChangeSupport pcs;
	private boolean pressed = false;
	private boolean dragged = false;
	private Rectangle rubberBandRectangle = null;

	public ImageCropper2() {
		super();
		this.x = 0;
		this.y = 0;
		this.height = Integer.MAX_VALUE;
		this.width = Integer.MAX_VALUE;
		this.image = null;
		setSize(100, 100);
		setBackground(Color.GRAY);
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public void mousePressed(MouseEvent e) {
		rubberBandRectangle = new Rectangle(e.getX(), e.getY(), 0, 0);
		pressed = true;
		dragged = false;
	}

	public void mouseDragged(MouseEvent e) {
		if (pressed) {
			dragged = true;
			updateLocation(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (rubberBandRectangle != null && dragged) {
			this.setX(rubberBandRectangle.x);
			this.setY(rubberBandRectangle.y);
			this.setWidth(rubberBandRectangle.width);
			this.setHeight(rubberBandRectangle.height);
			process();
			rubberBandRectangle = null;
		}
		pressed = false;
		dragged = false;
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void updateLocation(MouseEvent e) {
		if (rubberBandRectangle != null) {
			rubberBandRectangle.width = e.getX() - rubberBandRectangle.x;
			rubberBandRectangle.height = e.getY() - rubberBandRectangle.y;
		}

		repaint();
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 10.0f },
				0.0f));
		if (originalImage != null) {
			g.drawImage(originalImage.getImage().getAsBufferedImage(), 0, 0, this);
		}
		if (pressed && rubberBandRectangle != null) {
			g2d.drawRect(rubberBandRectangle.x, rubberBandRectangle.y, rubberBandRectangle.width,
					rubberBandRectangle.height);
		} else {
			g2d.drawRect(x, y, (int) Math.min(width, getSize().getWidth()),
					(int) Math.min(height, getSize().getHeight()));
		}
	}

	/**
	 * Gets the x-coordinate of the crop-rectangle.
	 * 
	 * @return the x-coordinate of the crop-rectangle
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Sets the x-coordinate of the crop-rectangle and fires a property change.
	 * 
	 * @param newX
	 *            the new value of the x-coordinate (>= 0)
	 */
	public void setX(int newX) {
		if (newX >= 0 && newX != x) {
			int oldX = x;
			x = newX;
			pcs.firePropertyChange("x", oldX, newX);
			process();
		}
	}

	/**
	 * Gets the y-coordinate of the crop-rectangle.
	 * 
	 * @return the y-coordinate of the crop-rectangle
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Sets the y-coordinate of the crop-rectangle and fires a property change.
	 * 
	 * @param newY
	 *            the new value of the y-coordinate (>= 0)
	 */
	public void setY(int newY) {
		if (newY >= 0 && newY != y) {
			int oldY = y;
			y = newY;
			pcs.firePropertyChange("y", oldY, newY);
			process();
		}
	}

	/**
	 * Gets the height of the crop-rectangle.
	 * 
	 * @return the height of the crop-rectangle
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Sets the height of the crop-rectangle and fires a property change.
	 * 
	 * @param newHeight
	 *            the new height of the crop-rectangle (>= 0)
	 */
	public void setHeight(int newHeight) {
		if (newHeight > 0 && newHeight != height) {
			int oldHeight = height;
			height = newHeight;
			pcs.firePropertyChange("height", oldHeight, newHeight);
			process();
		}
	}

	/**
	 * Gets the width of the crop-rectangle.
	 * 
	 * @return the width of the crop-rectangle
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Sets the width of the crop-rectangle and fires a property change.
	 * 
	 * @param newWidth
	 *            the new width of the crop-rectangle (>= 0)
	 */
	public void setWidth(int newWidth) {
		if (newWidth > 0 && newWidth != width) {
			int oldWidth = width;
			width = newWidth;
			pcs.firePropertyChange("width", oldWidth, newWidth);
			process();
		}
	}

	/**
	 * Gets the image.
	 * 
	 * @return the cropped image
	 */
	public ImageWrapper getImage() {
		return image;
	}

	public void setImage(ImageWrapper newImageWrapper) {
		originalImage = newImageWrapper != null ? newImageWrapper.clone() : null;
		process();
	}

	private void process() {
		if (originalImage != null) {
			try {
				ImageWrapper oldImage = image;
				ImageWrapper newImage = new itb5.filter.ImageCropper(new Rectangle(x, y, width, height),
						() -> originalImage.clone()).read();
				if (newImage != null) {
					image = newImage.clone();
				}
				repaint();
				pcs.firePropertyChange("image", oldImage, newImage);
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
