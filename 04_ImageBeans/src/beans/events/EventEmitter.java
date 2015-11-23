package beans.events;

import java.util.LinkedList;

public class EventEmitter<ListenerType, EventType> {
	private LinkedList<ListenerType> listeners = new LinkedList<>();
	
	public void addListener(ListenerType listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(ListenerType listener) {
		listeners.remove(listener);
	}
	
	public void emit(EventType data) {
		LinkedList<ListenerType> copiedListeners = null;
		synchronized (this) {
			copiedListeners = (LinkedList<ListenerType>) listeners.clone();
		}
		copiedListeners.forEach(listener -> listener.toString());
	}
}
