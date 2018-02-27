package com.hundun.rockmq2kafka.utils;

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

	private static String PATH = null;

	// load system property and configure
	static {
		// configure path
		String path = getSystemProperty(ConstantUtils.CONFIG_PATH);
		if (null == path)
			path = "./config";

		// environment path
		String env = getSystemProperty(ConstantUtils.ENV);
		if (null == env)
			env = "local";

		// format path
		PATH = path + "/" + env;
	}

	/**
	 * @DESC get KAFKA property
	 * @param key
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String getProperty(String key) throws FileNotFoundException, IOException {
		return getProperty(ConstantUtils.CFG_CONF, key);
	}

	/**
	 * @DESC read system property
	 * @param name
	 */
	private static String getSystemProperty(String key) {
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
	private static String getProperty(String path, String key) throws FileNotFoundException, IOException {

		// load properties every time
		Properties props = readProperty(PATH + "/" + path);
		return props.getProperty(key);
	}

	/**
	 * @DESC read property from input file
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static Properties readProperty(String path) throws FileNotFoundException, IOException {

		// load properties every time
		Properties props = new Properties();
		props.load(new FileInputStream(new File(path)));

		return props;
	}

}
