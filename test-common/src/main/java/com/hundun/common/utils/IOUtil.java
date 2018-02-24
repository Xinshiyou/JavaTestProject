package com.hundun.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @DESC 基础IO操作
 * @author saic_xinshiyou
 */
public class IOUtil {

	/**
	 * @DESC 根据输入流，读为字节流
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;

		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		inStream.close();

		return outSteam.toByteArray();
	}

}
