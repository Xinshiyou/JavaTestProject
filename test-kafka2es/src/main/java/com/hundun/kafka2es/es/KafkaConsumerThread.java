package com.hundun.kafka2es.es;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hundun.common.utils.PropertiesUtil;
import com.hundun.kafka2es.util.ConstantUtil;

public class KafkaConsumerThread implements Runnable {

	private final static Logger logger = LoggerFactory.getLogger(KafkaConsumerThread.class);
	private ConsumerRecords<String, String> records;
	private String root = null;

	public KafkaConsumerThread(ConsumerRecords<String, String> records, KafkaConsumer<String, String> consumer) {
		this.records = records;
		root = System.getProperty("user.dir") + "/conf/" + ConstantUtil.ES_CONF_FILE;
	}

	@Override
	public void run() {

		String index = null;
		try {
			index = PropertiesUtil.getProperty(root, "es.target.index");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		String type = null;
		try {
			type = PropertiesUtil.getProperty(root, "es.target.type");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		Map<String, Object> map = null;
		List<Map<String, Object>> list = null;

		for (TopicPartition partition : records.partitions()) {
			List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
			for (ConsumerRecord<String, String> record : partitionRecords) {
				JSONObject json = JSON.parseObject(record.value());
				list = new ArrayList<>();
				map = new HashMap<>();
				// index = String.format(index,
				// CalendarUtils.timeSpan2EsDay(json.getLongValue("_tm") *
				// 1000L));

				for (String key : json.keySet()) {
					if ("_uid".equals(key)) {
						map.put("uid", json.get(key));
					} else {
						map.put(key, json.get(key));
					}
					list.add(map);
				}

				EsUtils.write2Es(index, type, list);
			}
		}
	}

}