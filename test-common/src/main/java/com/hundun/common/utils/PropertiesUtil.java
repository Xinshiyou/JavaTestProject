package com.hundun.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @DESC read properties
 * @author xinshiyou
 */
public class PropertiesUtil {

	/**
	 * @DESC read system property
	 * @param name
	 */
	public static String getSystemProperty(String key) {
		String value = System.getProperty(key);
		if (value != null && value != "") {
			return value;
		}

		return null;
	}

	/**
	 * @DESC read property
	 * @param name
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String getProperty(String path, String key) throws FileNotFoundException, IOException {

		Properties props = readProperty(path);
		return props.getProperty(key);
	}

	/**
	 * @DESC read property from input file
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Properties readProperty(String path) throws FileNotFoundException, IOException {

		// load properties every time
		Properties props = new Properties();
		props.load(new FileInputStream(new File(path)));

		return props;
	}

}
