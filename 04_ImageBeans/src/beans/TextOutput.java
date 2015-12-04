package beans;

import java.awt.Dimension;
import java.awt.TextArea;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *	Prints {@link #value} into a textfield and fires {@link #value} as a "text".
 */
public class TextOutput extends TextArea {
	private static final long serialVersionUID = 1L;
	
	private PropertyChangeSupport pcs;
	private Object value;
	
	public TextOutput() {
		super();
		value = null;
		pcs = new PropertyChangeSupport(this);
		setSize(new Dimension(200, 50));
	}
	
	/**
	 * @return the content
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * @param value the content to set
	 */
	public void setValue(Object value) {
		if(value != null && !value.equals(this.value)) {
			Object oldValue = this.value;
			this.value = value;
			this.setText(value.toString());
			repaint();
			pcs.firePropertyChange("text", oldValue, value);
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
