package beans;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class ThresholdOperatorBeanInfo extends SimpleBeanInfo {
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor low = new PropertyDescriptor("low", ThresholdOperator.class);
			PropertyDescriptor high = new PropertyDescriptor("high", ThresholdOperator.class);
			PropertyDescriptor map = new PropertyDescriptor("map", ThresholdOperator.class);
			PropertyDescriptor image = new PropertyDescriptor("image", ThresholdOperator.class);
			return new PropertyDescriptor[] { low, high, map, image };
		} catch (Exception e) {
			return super.getPropertyDescriptors();
		}
	}
}
