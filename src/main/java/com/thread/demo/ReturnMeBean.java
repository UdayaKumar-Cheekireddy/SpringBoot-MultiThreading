package com.thread.demo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ReturnMeBean {

	private static final int noOfThreads = 20;

	
	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	
	public List<String> processMTOperation() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(noOfThreads);
//		Vector<String> strings = new Vector<>();
		List<String> strings = Collections.synchronizedList(new ArrayList<String>());
		long currentTimeMillis = System.currentTimeMillis();
			for (int i = 0; i < noOfThreads; i++) {
				threadPoolTaskExecutor.execute(new WorkerThread(i,strings,latch));
			}
		
		latch.await();		
		long currentEndTimeMillis = System.currentTimeMillis();
		System.out.println("totaltime:" + (currentEndTimeMillis - currentTimeMillis));
		System.out.println("String size:" + strings.size());
		return strings;
	}
	
	class WorkerThread implements Runnable {

		private int batchId;
		List<String> strings;
		private CountDownLatch latch;
		
		public WorkerThread(int batchId,List<String> strings,CountDownLatch latch) {
			this.batchId = batchId;
			this.strings = strings;
			this.latch = latch;
		}
		
		@Override
		public void run() {
			try {
				String s=UUID.randomUUID().toString()+":WorkerThread My Batch Id " + batchId;
				strings.add(s);
				Thread.sleep(2000);
				latch.countDown();
			} catch (InterruptedException e) {
			}
		}

	}
	
	
}
