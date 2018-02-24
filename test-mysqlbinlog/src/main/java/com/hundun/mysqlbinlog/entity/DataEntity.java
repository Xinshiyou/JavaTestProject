package com.hundun.mysqlbinlog.entity;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * @DESC
 * @author xinshiyou
 */
public class DataEntity {
	public String dbName = null;// database name
	public String tabName = null;// table name
	public String type = null;// SQL[create,drop] or delete,insert,update
	public String sql = null;// create or drop table SQL
	public String cols = null;// columns
	public List<?> rows = null;// rows data

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}