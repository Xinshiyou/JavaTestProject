package com.hundun.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author Administrator
 */
public class MD5Util {

	public static void main(String[] args) throws Exception {

		// Client client = getTransportClient();
		System.out.println(getMD5("mayun1fwoefjoerjqogqerojyoweroyjwoyjowyoqyjoqyjjyjqjytlqjykqj"));
		System.out.println(getMD5s("www.baidu.com"));
	}

	public static String getMD5(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] b = md5.digest(str.getBytes());
			BigInteger bi = new BigInteger(b).abs();
			String hex = bi.toString(16);

			// add 0 if <32
			if (hex.length() < 32) {
				StringBuilder strBuilder = new StringBuilder(hex);
				for (int i = hex.length(); i < 32; i++) {
					strBuilder.append('0');
				}
				hex = strBuilder.toString();
			}
			return hex;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" MD5 encrypt failed. Use original string: " + str + " instead. Please check.");
			return str;
		}

	}

	public static String getMD5s(String url) {
		// 可以自定义生成 MD5 加密字符传前的混合 KEY
		String key = "saic";
		// 要使用生成 URL 的字符
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };

		// encrypt by MD5

		String sMD5EncryptResult = null;

		sMD5EncryptResult = getMD5(key + url);

		String hex = sMD5EncryptResult;
		// 有可能会遇到hex只有31位的情况
		if (hex.length() < 32) {
			StringBuilder strBuilder = new StringBuilder(hex);
			for (int i = hex.length(); i < 32; i++) {
				strBuilder.append('0');
			}
			hex = strBuilder.toString();
		}
		String[] resUrl = new String[4];
		for (int i = 0; i < 4; i++) {
			// 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
			String sTempSubString = hex.substring(i * 8, i * 8 + 8);
			// 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
			// long ，则会越界
			long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
			String outChars = "";
			for (int j = 0; j < 6; j++) {
				// 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
				long index = 0x0000003D & lHexLong;
				// 把取得的字符相加
				outChars += chars[(int) index];
				// 每次循环按位右移 5 位
				lHexLong = lHexLong >> 5;
			}
			// 把字符串存入对应索引的输出数组
			resUrl[i] = outChars;
		}
		return resUrl[0];
	}

}
