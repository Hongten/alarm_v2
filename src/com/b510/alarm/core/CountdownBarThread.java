/**
 * 
 */
package com.b510.alarm.core;


/**
 * @author Hongten
 * @created 31 Dec, 2015
 */
public class CountdownBarThread implements Runnable {

	private volatile int current;
	private int amount;
	public boolean stop;

	public CountdownBarThread(int amount) {
		current = 0;
		this.amount = amount;
	}

	public void stopTimeBar() {
		stop = true;
	}

	public int getAmount() {
		return amount;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public void run() {
		while (!stop && current <= amount) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
			current = current + 1;
		}
	}
}
