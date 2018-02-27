package com.hundun.rockmq2kafka.utils;

public class ConstantUtils {

	// environment configure
	public static final String CONFIG_PATH = "cfgPath";// configure file's path
	public static final String ENV = "env";// concrete environment
	public static final String CFG_CONF = "conf.properties";// KAFKA configure

	/** kafka related configure */
	public static final String KAFKA_TOPIC = "kafka.topic";
	public static final String KAFKA_BOOST_SERVRE = "kafka.boot.servers";
	public static final String KAFKA_PRODUCER_RETRIES = "kafka.producer.retries";
	public static final String KAFKA_PRODUCER_ACKS = "kafka.producer.ack";
	public static final String KAFKA_PRODUCER_BATCH_SIZE = "kafka.batch.size";
	public static final String KAFKA_PRODUCER_BUFFER_MEMEORY = "kafka.buffer.size";// 100M

	/** rocket mq related configure */
	public static final String ROCKETMQ_TOPIC = "rocket.mq.topic";
	public static final String ROCKETMQ_CONSUMER_ID = "rocket.mq.consumer.id";
	public static final String ROCKETMQ_MESSAGE_MODEL = "rocket.mq.messagemodel";

}
