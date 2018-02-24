package com.hundun.kafka.producer;

import java.util.Properties;

public class SaicKafkaProducerFactory {

	// 单利模式
	public static synchronized SaicKafkaProducer getProducer() {

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "1");
		props.put("batch.size", 16384);// batch的大小
		props.put("linger.ms", 1);// 默认情况即使缓冲区有剩余的空间，也会立即发送请求，设置一段时间用来等待从而将缓冲区填的更多，单位为毫秒，producer发送数据会延迟1ms，可以减少发送到kafka服务器的请求数据
		props.put("buffer.memory", 33554432);// 提供给生产者缓冲内存总量
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("partitioner.class", "com.hundun.kafka.common.DefaultPartitioner");

		return new SaicKafkaProducer(props);
	}
}
