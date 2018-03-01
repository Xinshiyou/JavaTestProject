package com.hundun.kafka2es.es;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundun.common.utils.PropertiesUtil;
import com.hundun.kafka2es.util.ConstantUtil;

public class KafkaProducer {

	private final static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
	private final KafkaConsumer<String, String> consumer;
	private ExecutorService executorService;
	private String root = null;

	public KafkaProducer() throws FileNotFoundException, IOException {

		root = System.getProperty("user.dir") + "/conf/" + ConstantUtil.KAFKA_CONF_FILE;
		Properties props = new Properties();
		props.put("bootstrap.servers", PropertiesUtil.getProperty(root, "kafka.bootstrap.servers"));
		props.put("group.id", PropertiesUtil.getProperty(root, "consumer.group.id"));
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList(PropertiesUtil.getProperty(root, "kafka.consumer.topic")));

	}

	public void execute() {

		String threads = "";
		try {
			threads = PropertiesUtil.getProperty(root, "kafka.consumer.threads");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		if (null == threads || threads.length() < 1) {
			logger.error("Kafka configure invalidated!==>kafka.consumer.threads=" + threads);
			return;
		}
		executorService = Executors.newFixedThreadPool(Integer.parseInt(threads));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			if (null != records) {
				executorService.submit(new KafkaConsumerThread(records, consumer));
			}
		}

	}

	public void shutdown() {
		try {
			if (consumer != null) {
				consumer.close();
			}
			if (executorService != null) {
				executorService.shutdown();
			}
			if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				logger.error("Shutdown kafka consumer thread timeout.");
			}
		} catch (InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
	}

}
