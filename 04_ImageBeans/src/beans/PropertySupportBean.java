package beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * A base class for beans with property-change support.
 */
public abstract class PropertySupportBean implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(PropertySupportBean.class.getName());

	protected PropertyChangeSupport pcs;

	/**
	 * Initializes property-change support.
	 */
	public PropertySupportBean() {
		pcs = new PropertyChangeSupport(this);
	}

	/**
	 * Adds the specified listener to the list of property-change listeners.
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	/**
	 * Removes the specified listener from the list of property-change
	 * listeners.
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
