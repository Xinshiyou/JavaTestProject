package com.hundun.rockmq2kafka.kafka.common.partition;

/**
 * @DESC random distribute partition
 * @author saic_xinshiyou
 */
public class RandomPartition implements IPartition {

	@Override
	public int targetPartition(String topic, int totalPartitions, Object key) {

		int partitionNum = 0;
		partitionNum = key.hashCode();
		partitionNum = partitionNum % totalPartitions;

		return Math.abs(partitionNum);
	}

	@Override
	public void setObject(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public Object getObject(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
