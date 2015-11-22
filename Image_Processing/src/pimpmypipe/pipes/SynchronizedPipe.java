package pimpmypipe.pipes;

import java.io.StreamCorruptedException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import pimpmypipe.interfaces.IOable;

/**
 * This pipe can be used when working with multiple threads, connecting two filters that run in distinct threads.
 * Synchronization is ensured.
 * 
 * @param <T> the type to be handled by this pipe
 */
public class SynchronizedPipe<T> implements IOable<T, T> {
	private static final Logger _log = Logger.getLogger(SynchronizedPipe.class.getName());
	
	private static final Object _sentinel = new Object();
	private BlockingQueue<Object> _blockingQueue = new LinkedBlockingQueue<>();
	ExecutorService _executorService = Executors.newFixedThreadPool(2);
	
	@SuppressWarnings("unchecked")
	@Override
	public T read() throws StreamCorruptedException {
		try {
			Object element = _blockingQueue.take();
			if(element == _sentinel) {
				return null;
			} else {
				return (T) element;
			}
		} catch (InterruptedException e) {
			_log.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void write(T value) throws StreamCorruptedException {
		if(value != null) {
			try {
				_blockingQueue.put(value);
			} catch (InterruptedException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		} else {
			try {
				_blockingQueue.put(_sentinel);
			} catch (InterruptedException e) {
				_log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}
