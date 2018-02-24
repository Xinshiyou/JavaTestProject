package com.hundun.kafka.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class SaicKafkaConsumer {

	private static Properties props = new Properties();
	static {
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "crawler_id");
		props.put("enable.auto.commit", "true");// 自动提交offsets
		props.put("session.timeout.ms", "5000");// Consumer向集群发送自己的心跳，超时则认为Consumer已经死了，kafka会把它的分区分配给其他进程
		props.put("max.poll.interval.ms", "5000");// poll 最大时间间隔
		props.put("max.poll.records", "1");
		props.put("zookeeper.connect", "localhost");

		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");// 反序列化器
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("partitioner.class", "com.hundun.kafka.common.DefaultPartitioner");

	}

	public static void main(String[] args) {

		// props.put("partition.assignment.strategy", "range");// cousumer的分组id
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		// consumer.subscribe("crawler_test1");
		consumer.subscribe(Arrays.asList("crawler_test1"));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(5000);
			records.forEach(item -> {
				System.out.println("Message: " + item.key() + "\tValue:" + item.value());
			});
		}
	}

}
