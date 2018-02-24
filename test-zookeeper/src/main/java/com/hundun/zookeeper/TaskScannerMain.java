package com.hundun.zookeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;

/**
 * @DESC scanner tasks fail status
 * @author saic_xinshiyou
 */
public class TaskScannerMain {

	public static Logger logger = Logger.getLogger(TaskScannerMain.class);
	public static ExecutorService threadPool = Executors.newFixedThreadPool(1);

	public static String HOST = "localhost:2181";
	public static int SESSION_TIMEOUT = 30 * 1000;
	public static int CONNECTION_TIMEOUT = 2 * 1000;

	// ZK znode path
	public static String TEMP_PATH = "/tasks/temp";
	public static String PERM_PATH = "/tasks/perm";

	public static ZkClient getZKClient() {
		return new ZkClient(HOST, SESSION_TIMEOUT, CONNECTION_TIMEOUT);
	}

	/**
	 * @DESC add task to ZK
	 * @param taskUUID
	 * @param data
	 */
	public static void add(ZkClient zkClient, String taskUUID, Object data) {
		create(zkClient);
		try {
			zkClient.createEphemeral(TEMP_PATH + "/" + taskUUID, data);
			zkClient.createPersistent(PERM_PATH + "/" + taskUUID, data);
		} catch (Exception e) {
			logger.error("add to zookeeper failed!", e);
			// If one of two not exist, then delete both of two
			if (!zkClient.exists(PERM_PATH + "/" + taskUUID) || !zkClient.exists(TEMP_PATH + "/" + taskUUID)) {
				deleteTask(zkClient, taskUUID);
			}
		}
	}

	/**
	 * @DESC delete from ZK
	 * @param taskUUID
	 */
	public static void deleteTask(ZkClient zkClient, String taskUUID) {
		zkClient.delete(PERM_PATH + "/" + taskUUID);
		zkClient.delete(TEMP_PATH + "/" + taskUUID);
	}

	/**
	 * @DESC create ZK node
	 */
	public static void create(ZkClient zkClient) {
		if (!zkClient.exists(TEMP_PATH)) {
			subCreate(zkClient, TEMP_PATH);
		}
		if (!zkClient.exists(PERM_PATH)) {
			subCreate(zkClient, PERM_PATH);
		}
	}

	// loop for create paths
	private static void subCreate(ZkClient zkClient, String path) {
		int index = path.lastIndexOf("/");
		if (index > 0)
			subCreate(zkClient, path.substring(0, index));
		try {
			zkClient.createPersistent(path, path);
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		ZkClient client = getZKClient();

		client.writeData("/tasks/test/sub-0000000002", "XXXname");
	}
}
