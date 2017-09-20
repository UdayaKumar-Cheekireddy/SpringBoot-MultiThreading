package com.thread.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ReturnMeBeanCB {

	private static final int noOfThreads = 20;

	CyclicBarrier cyclicBarrier;
	
	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	List<String> strings = new ArrayList<String>();
	
	public List<String> processMTOperation() throws InterruptedException, BrokenBarrierException {
		cyclicBarrier = new CyclicBarrier(20);
		long currentTimeMillis = System.currentTimeMillis();
		for (int i = 0; i < noOfThreads; i++) {
			threadPoolTaskExecutor.execute(new WorkerThread(i));
		}
//		cyclicBarrier.await();
//		latch.await();
		long currentEndTimeMillis = System.currentTimeMillis();
		System.out.println("totaltime:" + (currentEndTimeMillis - currentTimeMillis));
		return strings;
	}
	
	class WorkerThread implements Runnable {

		int batchId;
		
		public WorkerThread(int batchId) {
			this.batchId = batchId;
		}
		
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				strings.add("CB My Batch Id " + batchId);
				try {
					cyclicBarrier.await();
				} catch (BrokenBarrierException e) {
				}
//				latch.countDown();
			} catch (InterruptedException e) {
			}
		}

	}
	
}
