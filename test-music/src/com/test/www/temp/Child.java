package com.test.www.temp;

public class Child extends Parent {

	public Child() {
		System.out.println("This is child's construct method");
	}

	@Override
	public void method() {
		System.out.println("This is child's method");
	}

}
