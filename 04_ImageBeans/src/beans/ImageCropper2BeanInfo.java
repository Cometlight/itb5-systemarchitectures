package beans;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class ImageCropper2BeanInfo extends SimpleBeanInfo {
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor image = new PropertyDescriptor("image", ImageCropper2.class);
			PropertyDescriptor x = new PropertyDescriptor("x", ImageCropper2.class);
			PropertyDescriptor y = new PropertyDescriptor("y", ImageCropper2.class);
			PropertyDescriptor width = new PropertyDescriptor("width", ImageCropper2.class);
			PropertyDescriptor height = new PropertyDescriptor("height", ImageCropper2.class);
			return new PropertyDescriptor[] { image, x, y, width, height };
		} catch (Exception e) {
			return super.getPropertyDescriptors();
		}
	}
}
