package com.hundun.rockmq2kafka.kafka.producer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;

import com.hundun.rockmq2kafka.kafka.common.GenericMessage;
import com.hundun.rockmq2kafka.utils.ConstantUtils;

/**
 * @DESC KAFKA producer class
 * @author xinshiyou
 */
public class OwithoKafkaProducer {

	private final static Logger logger = Logger.getLogger(OwithoKafkaProducer.class);
	private static AtomicInteger msgKey = new AtomicInteger(0);
	private KafkaProducer<Integer, String> producer = null;

	/** constructor */
	public OwithoKafkaProducer(Properties props) {
		this.producer = new KafkaProducer<Integer, String>(props);
	}

	/** increment key */
	public static int getIncrementAndGetKey() {
		return msgKey.getAndIncrement() + ((int) System.currentTimeMillis());
	}

	/** send record */
	public Future<RecordMetadata> send(ProducerRecord<Integer, String> record) {
		Future<RecordMetadata> result = producer.send(record);
		producer.flush();
		return result;
	}

	/** close */
	public void close() {
		producer.close();
	}

	/**
	 * @DESC test method
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args)
			throws InterruptedException, ExecutionException, NumberFormatException, FileNotFoundException, IOException {

		GenericMessage msg = new GenericMessage();
		ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(ConstantUtils.KAFKA_TOPIC,
				OwithoKafkaProducer.getIncrementAndGetKey(), msg.toString());

		OwithoKafkaProducer producer = KafkaProducerFactory.getInstance();
		Future<RecordMetadata> future = producer.send(record);
		RecordMetadata r = future.get();

		try {
			Thread.sleep(1000);
			producer.clone();
		} catch (InterruptedException e1) {
			logger.error("InterruptedException", e1);
		} catch (CloneNotSupportedException e) {
			logger.error("CloneNotSupportedException", e);
		}
	}

}
