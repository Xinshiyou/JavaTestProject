package com.hundun.odps;

import java.util.List;

import com.aliyun.odps.Instance;
import com.aliyun.odps.Odps;
import com.aliyun.odps.OdpsException;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.task.SQLTask;

/**
 * @DESC alibaba ODPS测试数据
 * @author xinshiyou
 */
public class Main extends App {

	private static final String accessId = "";
	private static final String accessKey = "";
	private static final String endPoint = "http://service.odps.aliyun.com/api";
	private static final String project = "";
	private static final String sql = "";

	public static void main(String[] args) {
		Account account = new AliyunAccount(accessId, accessKey);
		Odps odps = new Odps(account);
		odps.setEndpoint(endPoint);
		odps.setDefaultProject(project);
		Instance i;
		try {
			i = SQLTask.run(odps, sql);
			i.waitForSuccess();
			List<Record> records = SQLTask.getResult(i);
			for (Record r : records) {
				logger.info(r.get(0).toString());
			}
			System.out.println("Size:" + records.size());
		} catch (OdpsException e) {
			e.printStackTrace();
		}
	}
}