package com.test.www.phaser;

import java.util.concurrent.Phaser;

public class Student implements Runnable {

	private Phaser phaser;
	private String name;

	public Student(Phaser phaser, String name) {
		super();
		this.phaser = phaser;
		this.name = name;
	}

	@Override
	public void run() {

		System.out.println(String.format("Student[%s]已经达到考场", name));
		phaser.arriveAndAwaitAdvance();

		System.out.println(String.format("开始第一场测试：%s", name));
		doFirst();
		System.out.println(String.format("第一场测试结束：%s", name));

		phaser.arriveAndAwaitAdvance();

		System.out.println(String.format("开始第二场测试：%s", name));
		doSecond();
		System.out.println(String.format("第二场测试结束：%s", name));

		phaser.arriveAndAwaitAdvance();
	}

	private void doFirst() {
		System.out.println(String.format("学生[%s]测试一进行中...", name));
		try {
			Thread.sleep(Math.round(Math.random() * 1000));
		} catch (InterruptedException e) {
		}
	}

	private void doSecond() {
		System.out.println(String.format("学生[%s]测试二进行中...", name));
		try {
			Thread.sleep(Math.round(Math.random() * 1000));
		} catch (InterruptedException e) {
		}
	}

}
