package com.thread.demo;

import java.util.Timer;
import java.util.TimerTask;

public class TimerShutdown implements TimerShutdownCallback {

	int maxTime = 60;
	Timer timer = new Timer(true);

	public TimerShutdown(TimerTask timerTask) {
		timer.scheduleAtFixedRate(timerTask, 0, 60*100);
	}

	@Override
	public void shutdown() {
		maxTime = 0;
		timer.cancel();
	}

	@Override
	public void startCountdown() {
		while (maxTime > 0) {
			try {
				Thread.sleep(100*60);
				System.out.println("timer.. " + maxTime);
				synchronized (this) {
					maxTime--;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		shutdown();
	}

}
