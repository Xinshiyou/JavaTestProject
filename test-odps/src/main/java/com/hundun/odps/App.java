package com.hundun.odps;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @DESC Hello world!
 * @author xinshiyou
 */
public class App {

	/**
	 * test ODPS by java communication
	 */

	static {
		/** configure log4j */
		String path = "";// log4j.property path
		PropertyConfigurator.configure(path);
	}

	protected static final Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		System.out.println("Hello World!");
	}
}
