package com.hundun.java.thread.countdown;

import java.util.concurrent.CountDownLatch;

public class Main {

	public static void main(String[] args) {

		CountDownLatch cdl = new CountDownLatch(10);
		Thread t = null;
		for (int j = 0; j < 10; j++) {
			t = new Thread(new RunRR(cdl));
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// cdl.await();
		System.out.println("1236456798");
	}

	static class RunRR implements Runnable {

		private CountDownLatch cdl = null;

		public RunRR(CountDownLatch cdl) {
			this.cdl = cdl;
		}

		@Override
		public void run() {

			for (int i = 0; i < 10; i++) {
				System.out.println("线程：" + Thread.currentThread().getId());
				try {
					Thread.sleep(Math.round(Math.random() * 1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// cdl.countDown();
		}

	}

}
