package com.hundun.kafka.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class SaicKafkaProducer {

	private KafkaProducer<String, String> producer = null;

	private static AtomicInteger msgKey = new AtomicInteger(0);

	public static int getIncrementAndGetKey() {
		return msgKey.getAndIncrement() + ((int) System.currentTimeMillis());
	}

	public SaicKafkaProducer(Properties props) {
		this.producer = new KafkaProducer<String, String>(props);
	}

	public Future<RecordMetadata> send(ProducerRecord<String, String> record) {

		Future<RecordMetadata> result = producer.send(record);
		return result;
	}

	public void close() {
		producer.close();
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		String msg = "sxzdcfsdfd";
		ProducerRecord<String, String> record = new ProducerRecord<String, String>("crawler_test1", msg, msg);

		SaicKafkaProducer producer = SaicKafkaProducerFactory.getProducer();
		Future<RecordMetadata> future = producer.send(record);
		RecordMetadata r = future.get();
		System.out.println("===========>" + r.topic() + "======>123456");

		try {
			Thread.sleep(1000);
			producer.close();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("send over ------------------");
	}

}
