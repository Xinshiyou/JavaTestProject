package com.hundun.java.models.factory.method;

public class ConcreteCreator extends Creator {

	@Override
	public <T extends Product> T CreateProduct(Class<T> C) {
		Product produce = null;

		try {
			produce = (Product) Class.forName(C.getName()).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}

		return (T) produce;
	}

}
