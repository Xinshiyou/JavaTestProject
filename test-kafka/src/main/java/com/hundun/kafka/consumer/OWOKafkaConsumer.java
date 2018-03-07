package com.hundun.kafka.consumer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;

import com.hundun.common.utils.JDBCUtil;
import com.hundun.common.utils.PropertiesUtil;
import com.hundun.kafka.entity.DataEntity;
import com.hundun.kafka.utils.ConstantUtil;

/**
 * @DESC main class
 * @author xinshiyou
 */
public class OWOKafkaConsumer {

	protected static final String SQL = "insert into datax_table_variation(schema_name,sql_stmt,event_time) values (?,?,?)";

	private static final Logger logger = Logger.getLogger(OWOKafkaConsumer.class);
	private static Properties props = new Properties();
	private static String configPrefix = "./src/main/resources";
	private static volatile boolean flag = true;

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

	public static void main(String[] args) throws FileNotFoundException, IOException {

		// initialize jdbc connection
		final JDBCUtil jdbc = init();

		final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("crawler_test"));

		/** add shutdown hook */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				release(jdbc, consumer);
			}
		}));

		while (flag) {

			ConsumerRecords<String, String> records = consumer.poll(5000);
			System.out.println("Consumer Records: " + records.count());
			records.forEach(item -> {
				String content = item.value();
				System.out.println("read message from kafka : " + content);
				if (null != content && content.toLowerCase().contains("sql")) {

					DataEntity entity = DataEntity.parse(content);
					List<Object> datas = new ArrayList<Object>();
					if (entity.sql == null || entity.sql.trim().length() < 1)
						return;

					datas.add(entity.dbName);// schema name
					datas.add(entity.sql);// stmt sql
					datas.add(System.currentTimeMillis());// event time

					try {
						jdbc.insertStatement(SQL, datas);
					} catch (Exception e) {
						logger.error("Failed insert data to mysql databases", e);
						System.out.println("Failed insert data to mysql databases: " + e.getMessage());
					}
				}
			});

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

	/** shutdown method */
	public void shutdown() {
		flag = false;
		synchronized (this) {
			notifyAll();
		}
	}

	/** release connection and resources */
	private static void release(final JDBCUtil jdbc, final KafkaConsumer<String, String> consumer) {
		try {
			consumer.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			jdbc.releaseConn();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
