package com.hundun.rockmq2kafka.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.hundun.rockmq2kafka.kafka.producer.KafkaProducerFactory;
import com.hundun.rockmq2kafka.rocketmq.listener.RocketMQListener;
import com.hundun.rockmq2kafka.utils.ConstantUtils;
import com.hundun.rockmq2kafka.utils.PropertiesUtil;
import com.lingshou.lion.client.log.LoggerFactory;
import com.owitho.mq.api.MessageListenerApi;
import com.owitho.mq.api.Subscription;
import com.owitho.mq.bean.ConsumerBean;

/**
 * @DESC main class
 * @author xinshiyou
 */
public class AppMain {

	private static final com.lingshou.lion.client.log.Logger logger = LoggerFactory.getLogger(AppMain.class);

	public static void main(String[] args) throws FileNotFoundException, IOException {

		RocketMQListener rocketMQListener = new RocketMQListener();

		Subscription subscription = new Subscription();
		subscription.setTopic(PropertiesUtil.getProperty(ConstantUtils.ROCKETMQ_TOPIC));
		subscription.setExpression("*");

		Map<Subscription, MessageListenerApi> map = new HashMap<>();
		map.put(subscription, rocketMQListener);

		ConsumerBean consumerBean = new ConsumerBean();
		consumerBean.setConsumerId(PropertiesUtil.getProperty(ConstantUtils.ROCKETMQ_CONSUMER_ID));
		consumerBean.setSubscriptionTable(map);
		Properties properties = new Properties();
		properties.put("MessageModel", "CLUSTERING");
		String tmp = PropertiesUtil.getProperty(ConstantUtils.ROCKETMQ_MESSAGE_MODEL);
		if (null != tmp)
			properties.put("MessageModel", tmp);
		consumerBean.setProperties(properties);

		consumerBean.start();
		addHookListener(consumerBean);

	}

	/** shutdown hook listener */
	public static void addHookListener(final ConsumerBean consumerBean) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					consumerBean.shutdown();
				} catch (Exception e) {
					logger.warn("failed to shutdown consumer bean", e);
				}
				try {
					KafkaProducerFactory.getInstance().close();
				} catch (Exception e) {
					logger.warn("failed to shutdown kafka instance", e);
				}
			}
		});
	}

}
