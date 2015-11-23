package beans.events;

import java.util.EventListener;

public interface ImageListener extends EventListener {
	public abstract void imageValueChanged(ImageEvent event);
}
