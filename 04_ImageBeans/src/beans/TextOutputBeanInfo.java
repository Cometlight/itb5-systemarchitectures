package beans;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class TextOutputBeanInfo extends SimpleBeanInfo {
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor value = new PropertyDescriptor("value", TextOutput.class);
			return new PropertyDescriptor[] { value };
		} catch (Exception e) {
			return super.getPropertyDescriptors();
		}
	}
}
