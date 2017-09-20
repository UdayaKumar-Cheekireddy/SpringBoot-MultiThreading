package com.thread.demo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import ch.qos.logback.core.net.SyslogOutputStream;

@Component
public class TimeTaskShedular {

	public void process() {
		Map<Integer, Boolean> filelist = Collections.synchronizedMap(new HashMap<Integer, Boolean>());
		for (int i = 0; i < 10; i++) {
			filelist.put(i, false);
		}
		FileTask timerTask = new FileTask(filelist);
		TimerShutdown timerShutdown = new TimerShutdown(timerTask);
		timerTask.setTimerShutdownCallback(timerShutdown);
		timerShutdown.startCountdown();
		
		filelist.keySet().forEach(key-> System.out.println(key+"->"+filelist.get(key)));
		
		System.out.println("Process finished");
	}

	class FileTask extends TimerTask {

		private Map<Integer, Boolean> filelist;

		TimerShutdownCallback timerShutdownCallback;

		FileTask(Map<Integer, Boolean> filelist) {
			this.filelist = filelist;
		}

		FileTask(Map<Integer, Boolean> filelist, TimerShutdownCallback timerShutdownCallback) {
			this.filelist = filelist;
			this.timerShutdownCallback = timerShutdownCallback;
		}

		public void setTimerShutdownCallback(TimerShutdownCallback timerShutdownCallback) {
			this.timerShutdownCallback = timerShutdownCallback;
		}

		@Override
		public void run() {
				int id = ThreadLocalRandom.current().nextInt(3, 6 + 1);
				System.out.println(id + " Value before:" + filelist.get(id));
				filelist.replace(id, true);
				System.out.println(id + " Value after:" + filelist.get(id));
				if (id == 5) {
					timerShutdownCallback.shutdown();
				}
			

		}

	}
}
