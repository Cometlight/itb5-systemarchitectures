package itb5;

public class Timer {
	private long startTime = 0;
	private long endTime = 0;

	public void start() {
		this.startTime = System.nanoTime();
	}

	public void stop() {
		this.endTime = System.nanoTime();
	}

	public long getStartTime() {
		return this.startTime;
	}

	public long getEndTime() {
		return this.endTime;
	}

	public long getTotalTime() {
		return this.endTime - this.startTime;
	}
	
	public double getTotalTimeInSeconds() {
		return ((double)getTotalTime()) / 1000d / 1000d /1000d;
	}
}