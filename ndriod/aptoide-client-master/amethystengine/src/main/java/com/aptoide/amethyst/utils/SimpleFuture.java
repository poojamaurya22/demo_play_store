package com.aptoide.amethyst.utils;

/**
 * Created by neuro on 15-01-2016.
 */
public class SimpleFuture<T> {

	private T value;
	private int DELAY;

	public T get(int timeOutMillis) {
		int counter = 0;
		try {
			while (!isDone()) {
				DELAY = 100;
				Thread.sleep(DELAY);
				if (++counter * DELAY > timeOutMillis) {
					throw new InterruptedException("TimeOut reached! " + timeOutMillis);
				}
			}

			return value;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isDone() {
		return value != null;
	}

	public T get() {
		return this.get(5000);
	}

	public void set(T value) {
		this.value = value;
	}
}
