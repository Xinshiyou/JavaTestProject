package com.test.www.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MainTest {

	private static boolean flag = true;

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		while (flag) {

			String line = scan.nextLine().trim();
			if ("quit".equalsIgnoreCase(line)) {
				break;
			}

			Process p = null;
			try {
				p = Runtime.getRuntime().exec("hive -e \"" + line + "\"");
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String temp = null;
				while (null != (temp = br.readLine()))
					System.out.println("LOG:" + temp);
				if (null != br)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}

		}
		scan.close();

	}
}
