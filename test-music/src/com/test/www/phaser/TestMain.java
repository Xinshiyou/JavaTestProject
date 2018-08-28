package com.test.www.phaser;

import java.util.ArrayList;
import java.util.List;

public class TestMain {

	public static void main(String[] args) {

		
		List<Integer> result = new ArrayList<>();
 		TestPhaser phasers = new TestPhaser();
		Student[] students = new Student[5];
		for (int i = 0; i < 5; i++) {
			students[i] = new Student(phasers, "Name:" + (i + 1) + "");
			phasers.register();
		}

		for (int i = 0; i < 5; i++)
			new Thread(students[i]).start();

	}

}
