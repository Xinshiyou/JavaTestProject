package com.hundun.kafka.consumer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import com.hundun.common.utils.JDBCUtil;
import com.hundun.common.utils.PropertiesUtil;
import com.hundun.kafka.entity.DataEntity;
import com.hundun.kafka.utils.ConstantUtil;

public class OWOKafkaConsumer {

	protected static final String SQL = "insert into datax_table_variation(schema_name,sql_stmt,event_time) values (?,?,?)";

	private static final Logger logger = Logger.getLogger(OWOKafkaConsumer.class);
	private static Properties props = new Properties();
	private static String configPrefix = "./src/main/resources";
	private static boolean flag = true;

	static {

		String temp = "";
		try {
			temp = PropertiesUtil.getSystemProperty("env");
			if (null != temp)
				configPrefix = temp + "/conf";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		String zkServer = null;
		String kafkaBroker = null;
		boolean autoCommit = true;
		String groupID = null;

		try {
			zkServer = PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG, ConstantUtil.ZK_SERVER);
			groupID = PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG, ConstantUtil.KAFKA_GROUP_ID);
			kafkaBroker = PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG,
					ConstantUtil.KAFKA_BROKBER_LIST);
			autoCommit = Boolean.parseBoolean(PropertiesUtil.getProperty(configPrefix + ConstantUtil.KAFKA_CONFIG,
					ConstantUtil.KAFKA_AUTO_COMMIT));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		props.put("bootstrap.servers", kafkaBroker);
		props.put("group.id", groupID);
		props.put("enable.auto.commit", autoCommit);// 自动提交offsets
		props.put("session.timeout.ms", "5000");// Consumer向集群发送自己的心跳，超时则认为Consumer已经死了，kafka会把它的分区分配给其他进程
		props.put("max.poll.interval.ms", "5000");// poll 最大时间间隔
		props.put("max.poll.records", "1");
		props.put("zookeeper.connect", zkServer);

		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException, IOException {

		// initialize jdbc connection
		JDBCUtil jdbc = init();

		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
		consumer.subscribe(Arrays.asList("crawler_test"));
		while (flag) {
			ConsumerRecords<String, String> records = consumer.poll(5000);
			records.forEach(item -> {
				String content = item.value();
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
					}
				}
			});
		}
		try {
			jdbc.releaseConn();
		} catch (Exception e) {
			logger.error("Failed release connection to mysql databases", e);
		}

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

}
