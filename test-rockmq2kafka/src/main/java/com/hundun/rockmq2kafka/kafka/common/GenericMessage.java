package com.hundun.rockmq2kafka.kafka.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @DESC message body
 * @author xinshiyou
 */
public class GenericMessage implements Serializable {

	private final static Logger logger = Logger.getLogger(GenericMessage.class);
	private static final long serialVersionUID = 1L;

	private Map<String, Object> properties = null;

	// 消息时间
	private long timestamp;

	// 消息来源
	private String src = null;

	// 消息 源数据
	private Object data = null;

	public GenericMessage() {
		timestamp = System.currentTimeMillis();
		properties = new HashMap<String, Object>();
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties.putAll(properties);
	}

	public void setProperty(String key, Object value) {
		this.properties.put(key, value);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static byte[] javaSerialize(GenericMessage msg) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bo);
			oos.writeObject(msg);
			oos.flush();
			oos.close();
			bo.close();
		} catch (IOException e) {
			logger.error("message to bytes error: ", e);
		}
		return bo.toByteArray();
	}

	public static GenericMessage deSerialize(byte[] data) {
		GenericMessage msg = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream ois = new ObjectInputStream(bis);
			msg = (GenericMessage) ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return msg;
	}

	public GenericMessage highLevelCopy() {
		GenericMessage msg = new GenericMessage();
		msg.setSrc(this.src);
		msg.setProperties(properties);
		return msg;
	}
}
