package beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.logging.Logger;

public abstract class PropertySupportBean implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(PropertySupportBean.class.getName());
	
	protected PropertyChangeSupport pcs;
	
	public PropertySupportBean() {
		pcs = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
}
