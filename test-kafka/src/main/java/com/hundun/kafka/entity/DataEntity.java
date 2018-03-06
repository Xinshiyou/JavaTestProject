package com.hundun.kafka.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * @DESC data entity
 * @author xinshiyou
 */
@SuppressWarnings("serial")
public class DataEntity implements Serializable {

	/** global logger */
	protected transient static final Logger logger = Logger.getLogger(DataEntity.class);

	public String dbName = null;// database name
	public String tabName = null;// table name
	public String type = null;// SQL[create,drop] or delete,insert,update
	public String sql = null;// create or drop table SQL
	public String cols = null;// columns
	public List<String[]> rows = null;// rows data
	public List<String[]> brows = null;// before data for update mode

	/** serializable */
	@Override
	public String toString() {
		try {
			return JSON.toJSONString(this);
		} catch (Exception e) {
			logger.error("Serialize failed!", e);
			return null;
		}
	}

	/** deserializable */
	public static DataEntity parse(String input) {

		try {
			return JSON.parseObject(input, DataEntity.class);
		} catch (Exception e) {
			logger.error("Deserialize failed!", e);
			return null;
		}

	}

}