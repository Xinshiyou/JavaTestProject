package com.hundun.rockmq2kafka.kafka.producer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.hundun.rockmq2kafka.utils.ConstantUtils;
import com.hundun.rockmq2kafka.utils.PropertiesUtil;

/**
 * @DESC KAFKA producer factory
 * @author xinshiyou
 */
public class KafkaProducerFactory {

	/** local Logger */
	private final static Logger logger = Logger.getLogger(OwithoKafkaProducer.class);

	/** single instance model */
	private static class Instance {
		public static OwithoKafkaProducer instance = null;
		static {
			try {
				instance = KafkaProducerFactory.getProducer();
			} catch (IOException e) {
				logger.error("create kafka producer failed!", e);
			}
		}
	}

	/** get instance */
	public static OwithoKafkaProducer getInstance() {
		return Instance.instance;
	}

	/**
	 * @DESC add some property to our KAFKA producer
	 * @throws NumberFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static synchronized OwithoKafkaProducer getProducer() throws FileNotFoundException, IOException {

		OwithoKafkaProducer producer = null;
		Properties props = new Properties();
		props.put("bootstrap.servers", PropertiesUtil.getProperty(ConstantUtils.KAFKA_BOOST_SERVRE));
		props.put("retries", PropertiesUtil.getProperty(ConstantUtils.KAFKA_PRODUCER_RETRIES));
		props.put("acks", PropertiesUtil.getProperty(ConstantUtils.KAFKA_PRODUCER_ACKS));
		props.put("batch.size", PropertiesUtil.getProperty(ConstantUtils.KAFKA_PRODUCER_BATCH_SIZE));
		props.put("linger.ms", 1);// 默认情况即使缓冲区有剩余的空间，也会立即发送请求，设置一段时间用来等待从而将缓冲区填的更多，单位为毫秒，producer发送数据会延迟1ms，可以减少发送到kafka服务器的请求数据
		props.put("buffer.memory", PropertiesUtil.getProperty(ConstantUtils.KAFKA_PRODUCER_BUFFER_MEMEORY));// 提供给生产者缓冲内存总量
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("partitioner.class", "com.owitho.www.kafka.common.DefaultPartitioner");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		producer = new OwithoKafkaProducer(props);

		return producer;
	}
}
