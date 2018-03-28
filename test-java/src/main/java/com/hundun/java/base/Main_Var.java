package com.hundun.java.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @DESC test temporary
 * @author xinshiyou
 */
public class Main_Var {

	public static void main(String[] args) {
		test_9();
	}

	static void test_1() {
		int a = 100, b = 100, c = 200, d = 200;
		System.out.println("A==B?" + (a == b));
		System.out.println("C==D?" + (c == d));
	}

	static void test_2() {
		System.out.println(Math.round(11.5));
		System.out.println(Math.round(-11.5));
	}

	static void test_3() {
		String name = "Xinshiyou";
		System.out.println("Len:" + name.length());
	}

	static void test_4(String in, String out) throws IOException {

		FileInputStream fis = new FileInputStream(new File(in));
		FileOutputStream fos = new FileOutputStream(new File(out));

		byte[] tmp = new byte[4096];
		int len = -1;
		while ((len = fis.read(tmp)) != -1) {
			fos.write(tmp, 0, len);
		}

		fis.close();
		fos.flush();
		fos.close();

	}

	static void test_5(String in, String out) throws IOException {

		FileInputStream fis = new FileInputStream(new File(in));
		FileOutputStream fos = new FileOutputStream(new File(out));

		FileChannel inc = fis.getChannel();
		FileChannel ouc = fos.getChannel();
		ByteBuffer tmp = ByteBuffer.allocate(4096);

		while (inc.read(tmp) != -1) {
			tmp.flip();
			ouc.write(tmp);
			tmp.clear();
		}

		fis.close();
		fos.close();

	}

	/** test try(){}catch(){} */
	static void test_6(String in, String out) {

		try (FileInputStream fis = new FileInputStream(new File(in))) {

			fis.close();
		} catch (Exception e) {

		} finally {
		}

	}

	static void test_6() {
		A ab = new B();
		ab = new B();
	}

	static void test_7() {
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime yesterday = today.minusDays(1);
		System.out.println("Yestoday:" + yesterday);
	}

	static void test_8() {
		try {
			try {
				throw new Sneeze();
			} catch (Annoyance a) {
				System.out.println("Caught Annoyance");
				throw a;
			}
		} catch (Sneeze s) {
			System.out.println("Caught Sneeze");
			return;
		} finally {
			System.out.println("Hello World!");
		}
	}

	static void test_9() {
		String str = "北京市(朝阳区)(西城区)(海淀区)";
		Pattern p = Pattern.compile("(?<=\\().*?(?=\\))");
		Matcher m = p.matcher(str);
		while (m.find())
			System.out.println(m.group());

	}

	static void test_10() {

		String str = "rows=[Rowcolumns=[2, java], Rowcolumns=[3, ruby], Rowcolumns=[4, go], Rowcolumns=[5, python], Rowcolumns=[6, lua], Rowcolumns=[7, csharp], Rowcolumns=[8, ajax], Rowcolumns=[9, jsp]]";
		String matcher = "(?<=\\[Rowcolumns=\\[).*?(?=\\],)";
		Pattern p = Pattern.compile(matcher);
		Matcher m = p.matcher(str);
		while (m.find())
			System.out.println(m.group());

	}

}

class A {

	static {
		System.out.println("A");
	}

	public A() {
		System.out.println("AA");
	}

}

class B extends A {
	static {
		System.out.println("B");
	}

	public B() {
		System.out.println("BB");
	}
}

class Annoyance extends Exception {
}

class Sneeze extends Annoyance {
}
