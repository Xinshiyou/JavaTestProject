package com.hundun.gps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hundun.common.utils.JDBCUtil;

/**
 * @DESC 测试类
 * @author saic_xinshiyou
 */
public class Test {

	public static void main(String[] args) throws SQLException {
		mysqlAddressRX5();
	}

	/**
	 * @DESC RX5家庭地址、公司地址分析
	 * @throws SQLException
	 */
	public static void mysqlAddressRX5() throws SQLException {
		JDBCUtil db = new JDBCUtil();
		db.initConnection("", "", "");// initialize connection

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		String sql = "select distinct latitude,longitude,formatted_address from latlnginfos  where latitude > 30 and latitude < 32 and longitude > 120 and longitude < 123 and address_flag='company';";
		List<Map<String, Object>> list = db.findModeResult(sql, null);
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> tmp = list.get(i);
			String key = (String) tmp.get("formatted_address");
			if (key.startsWith("上海市")) {
				int index = key.indexOf("区");
				if (index < 0)
					index = key.indexOf("县");
				if (index < 0)
					System.out.println("地址异常： " + key);
				else {
					String val = key.substring(0, index + 1);
					if (map.containsKey(val)) {
						map.get(val).add(key);
					} else {
						List<String> lt = new ArrayList<String>();
						lt.add(key);
						map.put(val, lt);
					}
				}
			}
		}

		for (String t : map.keySet()) {
			List<String> ls = map.get(t);
			System.out.println(t + "\t" + ls.size());
		}
	}

	/**
	 * @desc 上海市GPS转化为百度地图坐标
	 * @throws SQLException
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void gpsTrasformTest() throws SQLException, NumberFormatException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("latlng.txt")));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result.txt")));
		String line = null;
		int n = 0;
		while (null != (line = br.readLine())) {
			if (line.length() < 1)
				continue;
			System.out.println("line no : " + n++);
			String[] tmp = line.split(",");
			double lat = Double.parseDouble(tmp[0]);
			double lng = Double.parseDouble(tmp[1]);
			double[] tp = GPSTransform.wgs_gcj_encrypts(lat, lng);
			double[] target = GPSTransform.google_bd_encrypt(tp[0], tp[1]);
			bw.write(target[0] + "," + target[1] + "\n");
		}
		bw.flush();
		bw.close();
		br.close();
	}

}