package com.hundun.kafka.consumer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

import com.hundun.common.utils.JDBCUtil;
import com.hundun.common.utils.PropertiesUtil;
import com.hundun.kafka.entity.DataEntity;
import com.hundun.kafka.utils.ConstantUtil;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * @DESC main class : kafka simple Java API
 * @author xinshiyou
 */
@SuppressWarnings("deprecation")
public class OWOKafkaStreamConsumer {

	protected static final String SQL = "insert into datax_table_variation(schema_name,sql_stmt,event_time) values (?,?,?)";

	private static final Logger logger = Logger.getLogger(OWOKafkaStreamConsumer.class);
	private static Properties props = new Properties();
	private static String configPrefix = "./src/main/resources";

	static {

		String temp = "";
		try {
			temp = PropertiesUtil.getSystemProperty("env");
			if (null != temp)
				configPrefix = temp + "/conf";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		String kafkaBroker = null;
		boolean autoCommit = true;
		String groupID = null;
		String zkServer = null;

		try {
			groupID = PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG, ConstantUtil.KAFKA_GROUP_ID);
			zkServer = PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG, ConstantUtil.ZK_SERVER);
			kafkaBroker = PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG,
					ConstantUtil.KAFKA_BROKBER_LIST);
			autoCommit = Boolean.parseBoolean(PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG,
					ConstantUtil.KAFKA_AUTO_COMMIT));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		props.put("bootstrap.servers", kafkaBroker);
		props.put("group.id", groupID);
		props.put("zookeeper.connect", zkServer);
		props.put("enable.auto.commit", autoCommit);// 自动提交offsets
		props.put("session.timeout.ms", "5000");// Consumer向集群发送自己的心跳，超时则认为Consumer已经死了，kafka会把它的分区分配给其他进程
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");

	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException, IOException {

		final JDBCUtil jdbc = init();
		String topic = PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG,
				ConstantUtil.KAFKA_TOPIC_NAME);
		ConsumerConfig config = new ConsumerConfig(props);
		final ConsumerConnector consumer = Consumer.createJavaConsumerConnector(config);

		/** add shutdown hook */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				release(jdbc, consumer);
			}
		}));

		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();

		while (it.hasNext()) {
			String content = new String(it.next().message());
			if (null != content && content.toLowerCase().contains("sql")) {

				DataEntity entity = DataEntity.parse(content);
				List<Object> datas = new ArrayList<Object>();
				if (entity.sql == null || entity.sql.trim().length() < 1)
					return;
				datas.add(entity.dbName);// schema name
				datas.add(entity.sql);// stmt sql
				datas.add(System.currentTimeMillis());// event time

				try {
					logger.debug("Insert one data into mysql: " + entity.toString());
					jdbc.insertStatement(SQL, datas);
				} catch (Exception e) {
					logger.error("Failed insert data to mysql databases", e);
				}
			}
		}
		release(jdbc, consumer);
	}

	/** initialize mysql connection */
	public static JDBCUtil init() throws FileNotFoundException, IOException {

		String url = PropertiesUtil.getProperty(configPrefix + ConstantUtil.MYSQL_CONFIG, ConstantUtil.MYSQL_URL);
		String userName = PropertiesUtil.getProperty(configPrefix + ConstantUtil.MYSQL_CONFIG, ConstantUtil.MYSQL_USER);
		String passwd = PropertiesUtil.getProperty(configPrefix + ConstantUtil.MYSQL_CONFIG, ConstantUtil.MYSQL_PASSWD);

		JDBCUtil jdbc = new JDBCUtil();
		jdbc.initConnection(url, userName, passwd);

		return jdbc;

	}

	/** release connection and resources */
	private static void release(final JDBCUtil jdbc, final ConsumerConnector consumer) {

		try {
			if (null != consumer)
				consumer.shutdown();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			if (null != jdbc)
				jdbc.releaseConn();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
