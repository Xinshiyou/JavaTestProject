package com.hundun.kafka.consumer.lowapi;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;
import com.hundun.common.utils.PropertiesUtil;

/**
 * @DESC main class
 * @author xinshiyou
 */
public class Main {

	private static final Logger logger = Logger.getLogger(Main.class);
	private static String configPrefix = "./src/main/resources";
	static {
		String temp = "";
		try {
			temp = PropertiesUtil.getSystemProperty("env");
			if (null != temp)
				configPrefix = temp + "/conf";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		KafkaSimpleConsumer consumer = new KafkaSimpleConsumer();
		consumer.init(configPrefix);
		consumer.run();
	}

}