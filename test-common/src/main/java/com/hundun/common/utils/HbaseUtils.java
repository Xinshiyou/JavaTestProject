package com.hundun.common.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

/**
 * @DESC HBASE operation handler
 * @author xinshiyou
 */
public final class HbaseUtils {

	private static final Logger logger = Logger.getLogger(HbaseUtils.class);

	private static Connection conn = null;
	private static Configuration configuration = null;

	private static String zookeeerQuorum = null;
	private static String zookeeperPort = null;
	private static String hbaseVersion = null;

	/**
	 * @DESC initialize
	 */
	public static void init(String fileName) {

		// read configure from conf file or other way
		try {
			if (null == configuration) {
				configuration.set("hbase.zookeeper.quorum", zookeeerQuorum);
				configuration.set("hbase.zookeeper.property.clientPort", zookeeperPort);
				configuration.set("hbase.defaults.for.version.skip", hbaseVersion);
			}
		} catch (Exception e) {
			logger.error("HBase Configuration Initialization failure !");
			throw new RuntimeException(e);
		}
	}

	/**
	 * @DESC 获得链接
	 * @return
	 */
	public static synchronized Connection getConnection() {

		try {
			if (conn == null || conn.isClosed())
				conn = ConnectionFactory.createConnection(configuration);
		} catch (IOException e) {
			logger.error("HBase establish a connection ", e);
		}

		return conn;
	}

	/**
	 * @DESC check table exists
	 * @param tableName
	 */
	public static boolean checkTableExists(String tableName) {

		HBaseAdmin hBaseAdmin = null;
		try {
			Connection conn = getConnection();
			hBaseAdmin = (HBaseAdmin) conn.getAdmin();
			return hBaseAdmin.tableExists(tableName);
		} catch (MasterNotRunningException e) {
			logger.error(e.getMessage(), e);
		} catch (ZooKeeperConnectionException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (null != hBaseAdmin)
				try {
					hBaseAdmin.close();
				} catch (IOException e) {
				}
		}

		return false;
	}

	public static void createNamespace(String namespace) throws IOException {

		Connection conn = getConnection();
		HBaseAdmin hBaseAdmin = (HBaseAdmin) conn.getAdmin();
		hBaseAdmin.createNamespace(NamespaceDescriptor.create(namespace).build());

	}

	/**
	 * @DESC 创建表:如果已经存在，那么删除
	 * @param tableName
	 */
	public static void createTable(String tableName, Set<String> columnFamilys) {

		try {
			Connection conn = getConnection();
			HBaseAdmin hBaseAdmin = (HBaseAdmin) conn.getAdmin();
			if (hBaseAdmin.tableExists(tableName)) {
				logger.info("Table < " + tableName + " > already exists");
				return;
			}
			HTableDescriptor tableDescriptor = hBaseAdmin.getTableDescriptor(TableName.valueOf(tableName));
			Iterator<String> iter = columnFamilys.iterator();
			while (iter.hasNext())
				tableDescriptor.addFamily(new HColumnDescriptor(Bytes.toBytes(iter.next())));
			hBaseAdmin.createTable(tableDescriptor);
		} catch (MasterNotRunningException e) {
			logger.error(e.getMessage(), e);
		} catch (ZooKeeperConnectionException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * @DESC 删除一张表
	 * @param tableName
	 */
	public static void dropTable(String tableName) {
		try {
			Connection conn = getConnection();
			HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
		} catch (MasterNotRunningException e) {
			logger.info(e.getMessage(), e);
		} catch (ZooKeeperConnectionException e) {
			logger.info(e.getMessage(), e);
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}

	}

}
