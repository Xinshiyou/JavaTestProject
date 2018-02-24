package com.hundun.java.thread.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main implements Runnable {
	private int id;
	private CyclicBarrier barrier;

	public Main(int id, final CyclicBarrier barrier) {
		this.id = id;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		try {
			System.out.println(id + " th people wait");
			barrier.await(); // 大家等待最后一个线程到达
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println("ID:" + id);
	}
}
