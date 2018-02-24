package com.hundun.java.models.factory.method;

public abstract class Product {

	public void method() {
		// 公共业务逻辑
		System.out.println("这是公共逻辑");
	}

	public abstract void method2();

}
