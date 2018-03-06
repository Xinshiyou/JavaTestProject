package com.hundun.kafka.utils;

/**
 * @DESC
 * @author xinshiyou
 */
public class ConstantUtil {

	public static final String KAFKA_CONFIG = "kafka.properties";
	public static final String MYSQL_CONFIG = "mysql.properties";

	/** kafka properties */
	public static final String KAFKA_BROKBER_LIST = "kafka.broker.list";
	public static final String KAFKA_GROUP_ID = "kafka.consumer.group.id=binlog-consumer-ddl";
	public static final String KAFKA_AUTO_COMMIT = "enable.auto.commit";

	/** zookeeper properties */
	public static final String ZK_SERVER = "zookeeper.server";

	/** mysql properties */
	public static final String MYSQL_URL = "mysql.connect.url";
	public static final String MYSQL_USER = "account.user.name";
	public static final String MYSQL_PASSWD = "account.user.password";

	/** table metas */
	public static final String TABLE_SCHEMA_NAME = "schema_name";
	public static final String TABLE_SQL_STMT = "sql_stmt";
	public static final String TABLE_EVENT_TIME_ = "event_time";

}
