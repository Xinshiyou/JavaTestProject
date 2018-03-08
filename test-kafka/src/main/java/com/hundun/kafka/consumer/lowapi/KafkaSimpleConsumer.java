package com.hundun.kafka.consumer.lowapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.hundun.common.utils.JDBCUtil;
import com.hundun.common.utils.PropertiesUtil;
import com.hundun.kafka.utils.ConstantUtil;

public class KafkaSimpleConsumer {

	private static final Logger logger = LogManager.getLogger(KafkaSimpleConsumer.class);

	private KafkaConfig kafkaConfig = null;
	private ExecutorService executor = null;
	private boolean inited = false;
	private JDBCUtil jdbc = null;

	public void run() {

		if (!inited) {
			logger.error("uninit, init first!");
			return;
		}

		File file = new File(kafkaConfig.checkpoint);
		if (!file.exists()) {
			file.mkdir();
		}

		executor = Executors.newFixedThreadPool(kafkaConfig.partitionNum);
		for (int threadNum = 0; threadNum < kafkaConfig.partitionNum; ++threadNum) {
			file = new File(kafkaConfig.checkpoint + "/partition" + threadNum);
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();
			}
			logger.info("begin submit partition msg task thread");
			executor.submit(new PartitionMsgTask(jdbc, kafkaConfig, threadNum));
		}
	}

	public int init(String prefixPath) {

		Properties props = new Properties();
		kafkaConfig = new KafkaConfig();
		try {
			FileInputStream in = new FileInputStream(prefixPath + ConstantUtil.KAFKA_CONFIG);
			props.load(in);
		} catch (FileNotFoundException e) {
			logger.error("kafka config file not found. file name:" + ConstantUtil.KAFKA_CONFIG);
			return -1;
		} catch (IOException e) {
			logger.error("properties load file failed");
			return -1;
		}

		kafkaConfig.topic = props.getProperty("topic");
		kafkaConfig.port = Integer.parseInt(props.getProperty("port"));
		kafkaConfig.partitionNum = Integer.parseInt(props.getProperty("partitionNum"));
		kafkaConfig.checkpoint = props.getProperty("checkpoint");
		kafkaConfig.patchSize = Integer.parseInt(props.getProperty("patchSize"));
		String startPoint = props.getProperty("subscribeStartPoint");
		if (!startPoint.equals("latest") && !startPoint.equals("earliest")) {
			logger.error("config file startPoint error. startPoint must be latest or earliest");
			return -1;
		}
		kafkaConfig.subscribeStartPoint = startPoint;
		String brokerList = props.getProperty("brokerList");
		String[] brokers = brokerList.split(",");
		kafkaConfig.replicaBrokers = new ArrayList<String>();
		for (String str : brokers) {
			kafkaConfig.replicaBrokers.add(str);
		}
		inited = true;
		logger.info("init success. kafkaConfig:" + kafkaConfig.toString());

		String url = null;
		String userName = null;
		String passwd = null;
		try {
			url = PropertiesUtil.getProperty(prefixPath + ConstantUtil.MYSQL_CONFIG, ConstantUtil.MYSQL_URL);
			userName = PropertiesUtil.getProperty(prefixPath + ConstantUtil.MYSQL_CONFIG, ConstantUtil.MYSQL_USER);
			passwd = PropertiesUtil.getProperty(prefixPath + ConstantUtil.MYSQL_CONFIG, ConstantUtil.MYSQL_PASSWD);
		} catch (IOException e) {
			logger.error("config file error", e);
			return -1;
		}

		jdbc = new JDBCUtil();
		jdbc.initConnection(url, userName, passwd);

		logger.info("init success. m:" + url + "\t" + userName + "\t" + passwd);

		return 0;
	}

}