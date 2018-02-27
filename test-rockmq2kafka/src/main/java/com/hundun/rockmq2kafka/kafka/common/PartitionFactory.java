package com.hundun.rockmq2kafka.kafka.common;

import com.hundun.rockmq2kafka.kafka.common.partition.IPartition;
import com.hundun.rockmq2kafka.kafka.common.partition.RandomPartition;

/**
 * @DESC 选择分配partition的策略
 * @author saic_xinshiyou
 */
public class PartitionFactory {

	private static PartitionFactory instance = null;

	private PartitionFactory() {
	}

	public static PartitionFactory getInstance() {
		if (null == instance)
			instance = new PartitionFactory();
		return instance;
	}

	public IPartition getStrategy() {

		IPartition result = null;
		result = new RandomPartition();

		return result;
	}

}
