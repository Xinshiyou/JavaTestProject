package com.hundun.rockmq2kafka.kafka.common;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import com.hundun.rockmq2kafka.kafka.common.partition.IPartition;

import kafka.utils.VerifiableProperties;

public class DefaultPartitioner implements Partitioner {

	/**
	 * @DESC 无参构造函数
	 */
	public DefaultPartitioner() {
		this(new VerifiableProperties());
	}

	/**
	 * @DESC 构造函数，必须给定
	 * @param properties:上下文
	 */
	public DefaultPartitioner(VerifiableProperties properties) {
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

		List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
		int numPartitions = partitions.size();

		IPartition strategy = PartitionFactory.getInstance().getStrategy();
		try {
			return strategy.targetPartition(topic, numPartitions, key);
		} catch (Exception e1) {
			return Math.abs(key.hashCode() % numPartitions);
		}
	}

	@Override
	public void configure(Map<String, ?> configs) {
	}

	@Override
	public void close() {
	}

}
