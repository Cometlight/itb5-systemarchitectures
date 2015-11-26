package beans;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class ThresholdOperatorBeanInfo extends SimpleBeanInfo {
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor low = new PropertyDescriptor("Low Value", ThresholdOperator.class, "getLow", "setLow");
			PropertyDescriptor high = new PropertyDescriptor("High Value", ThresholdOperator.class, "getHigh", "setHigh");
			PropertyDescriptor map = new PropertyDescriptor("Map Value", ThresholdOperator.class, "getMap", "setMap");
			PropertyDescriptor image = new PropertyDescriptor("Image", ThresholdOperator.class, "getImage", "setImage");
			return new PropertyDescriptor[] { low, high, map, image };
		} catch (Exception e) {
			return super.getPropertyDescriptors();
		}
	}
}
