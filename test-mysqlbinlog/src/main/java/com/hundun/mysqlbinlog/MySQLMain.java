package com.hundun.mysqlbinlog;

import java.io.IOException;
import java.util.BitSet;
import java.util.List;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.hundun.mysqlbinlog.entity.DataEntity;

/**
 * @DESC test class
 * @author xinshiyou
 */
public class MySQLMain {

	/**
	 * @DESC 优化空间：<br>
	 *       1、添加多线程处理<br>
	 *       2、添加分布式监听，防止单点故障<br>
	 *       3、其余优化空间
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {

		BinaryLogClient client = new BinaryLogClient("lmaster", 3306, "canal", "canal");
		client.registerEventListener(new EventListener() {

			private DataEntity entity = null;
			private boolean flag = false;

			@Override
			public void onEvent(Event event) {

				System.out.println("Event:" + event);

				/**
				 * 总结:<br>
				 * 1、create or drop table:只有一个事件相应【 QueryEventData 】，里面可以看到SQL语句
				 * 2、insert,update,delete:多个事件组成，包括Query/Table_Map/EXT_XXX等事件组成
				 */
				String type = event.getHeader().getEventType().name();
				if (null != type && !flag) {

					System.out.println("Type:==" + type + "==" + (type.startsWith("EXT")));
					if (null == entity && type.equals("QUERY")) {

						entity = new DataEntity();
						if (null != event.getData()) {// create or drop table
							QueryEventData qed = event.getData();
							String sql = qed.getSql();
							if (!"BEGIN".equals(sql)) {
								entity.dbName = qed.getDatabase();
								entity.sql = sql;
								entity.type = "SQL";
								flag = true;
							}
						}
					} else if (null != entity && type.equals("TABLE_MAP")) {

						TableMapEventData tme = event.getData();
						entity.dbName = tme.getDatabase();
						entity.tabName = tme.getTable();

					} else if (null != entity && type.startsWith("EXT")) {

						flag = true;
						BitSet bs = null;
						List<?> rows = null;

						EventData dt = event.getData();

						if (dt instanceof WriteRowsEventData) {

							WriteRowsEventData wre = event.getData();
							bs = wre.getIncludedColumns();
							rows = wre.getRows();
							entity.type = "insert";

						} else if (dt instanceof DeleteRowsEventData) {

							DeleteRowsEventData wre = event.getData();
							bs = wre.getIncludedColumns();
							rows = wre.getRows();
							entity.type = "delete";

						} else if (dt instanceof UpdateRowsEventData) {

							UpdateRowsEventData wre = event.getData();
							bs = wre.getIncludedColumns();
							rows = wre.getRows();
							entity.type = "update";
						}

						entity.cols = bs.toString();
						entity.rows = rows;
					}
				}

				if (flag) {
					entity = null;
					flag = false;
				}
			}
		});

		// client.setHeartbeatInterval(1000);
		client.connect();
	}

}
