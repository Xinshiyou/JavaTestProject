package com.hundun.java.java8.stream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Main {

	public static void main(String[] args) {
		test1();
	}

	public static void test1() {

		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("e");
		list.add("e");

		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println("Item:" + t + "ops");
			}
		});

		Optional<String> result = list.stream().max(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		System.out.println("Result:" + result);

	}

}
