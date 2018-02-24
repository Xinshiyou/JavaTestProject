package com.hundun.java.annotations;

import java.lang.reflect.Method;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Class<?> businessLogicClass = Student.class;

		System.out.println("Size Method:"
				+ businessLogicClass.getMethods().length);
		for (Method method : businessLogicClass.getMethods()) {
			DO todoAnnotation = (DO) method.getAnnotation(DO.class);
			if (todoAnnotation != null) {
				System.out.println(" Method Name : " + method.getName());
				System.out.println(" Name : " + todoAnnotation.name());
				System.out.println(" Age : " + todoAnnotation.age());
				System.out.println(" ID : " + todoAnnotation.id());
			}
		}

	}

}
