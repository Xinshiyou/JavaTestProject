package com.hundun.java.annotations;

/**
 * @DESC student annotations
 * @author saic_xinshiyou
 */
public @interface DO {

	String name() default "";

	int age() default -1;

	String id() default "";

}
