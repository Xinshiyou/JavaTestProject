package com.hundun.rockmq2kafka.rocketmq.listener;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hundun.rockmq2kafka.entity.UserEvent;
import com.hundun.rockmq2kafka.kafka.producer.KafkaProducerFactory;
import com.hundun.rockmq2kafka.kafka.producer.OwithoKafkaProducer;
import com.hundun.rockmq2kafka.utils.ConstantUtils;
import com.lingshou.lion.client.log.LoggerFactory;
import com.owitho.mq.api.ConsumerState;
import com.owitho.mq.api.Message;
import com.owitho.mq.api.MessageListenerApi;

/**
 * @DESC listener
 * @author xinshiyou
 */
public class RocketMQListener implements MessageListenerApi {

	private static final com.lingshou.lion.client.log.Logger logger = LoggerFactory.getLogger(RocketMQListener.class);

	private final Gson gson = new Gson();
	private OwithoKafkaProducer kafkaProducer = null;

	@Override
	public ConsumerState consume(Message arg0) {

		try {

			String content = new String(arg0.getBody(), Charset.forName("UTF-8"));
			List<UserEvent> userEvents = gson.fromJson(content, new TypeToken<List<UserEvent>>() {
			}.getType());
			userEvents.forEach(item -> {
				sendMessage(item);
			});

			return ConsumerState.CommitMessage;
		} catch (Exception e) {
			logger.error("RocketMQ commit status failed!", e);
			return ConsumerState.ReconsumeLater;
		}
	}

	/** send message to Kafka */
	private void sendMessage(UserEvent userEvent) {

		if (null == kafkaProducer)
			kafkaProducer = KafkaProducerFactory.getInstance();

		ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(ConstantUtils.KAFKA_TOPIC,
				OwithoKafkaProducer.getIncrementAndGetKey(), gson.toJson(userEvent));
		kafkaProducer.send(record);
		logger.info("发送数据到Kafka:" + gson.toJson(userEvent));

	}

}
