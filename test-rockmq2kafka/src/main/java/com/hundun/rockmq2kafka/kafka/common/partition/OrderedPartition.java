package com.hundun.rockmq2kafka.kafka.common.partition;

/**
 * @DESC look-order distribute partitionsO
 * @author saic_xinshiyou
 */
public class OrderedPartition implements IPartition {

	/**
	 * @DESC 需要外部保证输入的为可解析为整数的字符串，且保证序列
	 */

	@Override
	public int targetPartition(String topic, int totalPartitions, Object key) throws Exception {

		int partitionNum = Integer.parseInt((String) key);// 外部处理异常
		return Math.abs(partitionNum % totalPartitions);
	}

	@Override
	public void setObject(Object obj) {
	}

	@Override
	public Object getObject(Object obj) {
		return null;
	}

}
