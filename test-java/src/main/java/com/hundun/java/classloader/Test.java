package com.hundun.java.classloader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException,
			InvocationTargetException {

		Class<?> cls = ClassLoader.getSystemClassLoader().loadClass("test");
		Method method = cls.getDeclaredMethod("printMsg");
		Object obj = cls.newInstance();
		Object md = method.invoke(obj);
		System.out.println("Method: " + md);
	}

}
