package com.hundun.kafka;

import java.util.ArrayList;
import java.util.List;

public class AppTest {

	public static void main(String[] args) {

		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.add("e");

		list.forEach(item -> {
			if (item == null || item.equalsIgnoreCase("a"))
				return;
			System.out.println("Item: " + item);
		});

	}

}
