package com.hundun.java.models.factory.method;

public abstract class Creator {

	public abstract <T extends Product> T CreateProduct(Class<T> C);

}
