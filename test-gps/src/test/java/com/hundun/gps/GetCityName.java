package com.hundun.gps;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hundun.common.utils.JDBCUtil;

/**
 * @desc 根据输入经纬度，得到该位置的名称／周边商圈等数据
 * @author saic_xinshiyou
 */
public class GetCityName {

	public static final String ak1 = "";
	public static final String ak2 = "";
	public static final String ak = "";

	public static final String sql = "insert into latlnginfos(vin,week,date,diff_time,begin_hour,end_hour,latitude,longitude,formatted_address,distance,poiType,name,tag,pointX,pointY) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * @param addr
	 *            查询的地址
	 * @return
	 * @throws IOException
	 */
	public Object[] getCoordinate(String addr) throws IOException {
		String lng = null;// 经度
		String lat = null;// 纬度
		String address = null;
		try {
			address = java.net.URLEncoder.encode(addr, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String key = "f247cdb592eb43ebac6ccd27f796e2d2";
		String url = String.format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key);
		URL myURL = null;
		URLConnection httpsConn = null;
		try {
			myURL = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStreamReader insr = null;
		BufferedReader br = null;
		try {
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
			if (httpsConn != null) {
				insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				br = new BufferedReader(insr);
				String data = null;
				int count = 1;
				while ((data = br.readLine()) != null) {
					if (count == 5) {
						lng = (String) data.subSequence(data.indexOf(":") + 1, data.indexOf(","));// 经度
						count++;
					} else if (count == 6) {
						lat = data.substring(data.indexOf(":") + 1);// 纬度
						count++;
					} else {
						count++;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (insr != null) {
				insr.close();
			}
			if (br != null) {
				br.close();
			}
		}
		return new Object[] { lng, lat };
	}

	public static String getCity(String vin, int week, String date, long diff_time, int begin_hour, int end_hour,
			double lat, double lng, JDBCUtil jdbcUtils) throws IOException {
		String add = "http://api.map.baidu.com/geocoder/v2/?ak=" + ak + "&location=" + lat + "," + lng
				+ "&output=json&pois=1";
		URL myURL = null;
		URLConnection httpsConn = null;
		try {
			myURL = new URL(add);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		InputStreamReader insr = null;
		BufferedReader br = null;
		try {
			httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
			if (httpsConn != null) {
				insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
				br = new BufferedReader(insr);
				String data = null;
				while ((data = br.readLine()) != null) {
					JSONObject obj = new JSONObject(data);
					System.out.println(obj);
					JSONArray jarray = obj.getJSONObject("result").getJSONArray("pois");
					String formatted_address = obj.getJSONObject("result").getString("formatted_address");
					for (int i = 0; i < jarray.length(); i++) {
						JSONObject jobj = jarray.getJSONObject(i);
						List<Object> params = new ArrayList<Object>();
						params.add(vin);
						params.add(week);
						params.add(date);
						params.add(diff_time);
						params.add(begin_hour);
						params.add(end_hour);
						params.add(lat);
						params.add(lng);
						params.add(formatted_address);
						params.add(jobj.getString("distance"));
						params.add(jobj.getString("poiType"));
						params.add(jobj.getString("name"));
						params.add(jobj.getString("tag"));
						params.add(jobj.getJSONObject("point").getDouble("x"));
						params.add(jobj.getJSONObject("point").getDouble("y"));
						try {
							jdbcUtils.updateByPreparedStatement(sql, params);
						} catch (SQLException e) {
							System.out.println("写入数据库错误!");
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != insr)
				insr.close();
			if (null != br)
				br.close();
		}

		return null;
	}

	public static void main(String[] args) throws IOException {

		final JDBCUtil jdbcUtils = new JDBCUtil();
		jdbcUtils.initConnection("", "", "");// set URL, user name and password

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("latlng.txt")));
		String line = null;
		int n = 0;
		while (null != (line = br.readLine())) {
			if (line.length() < 1)
				continue;
			System.out.println("line no : " + n++);
			String[] tmp = line.split(",");
			String vin = tmp[0];
			int week = Integer.parseInt(tmp[1]);
			String date = tmp[2];
			long diff_time = Long.parseLong(tmp[3]);
			int begin_hour = Integer.parseInt(tmp[4]);
			int end_hour = Integer.parseInt(tmp[5]);
			double lat = Double.parseDouble(tmp[6]);
			double lng = Double.parseDouble(tmp[7]);

			GetCityName.getCity(vin, week, date, diff_time, begin_hour, end_hour, lat, lng, jdbcUtils);

		}
		jdbcUtils.releaseConn();
		br.close();
	}

}

class myRun implements Runnable {

	String vin;
	int week;
	String date;
	long diff_time;
	int begin_hour;
	int end_hour;
	double lat;
	double lng;
	JDBCUtil jdbcUtils;

	public myRun(String vin, int week, String date, long diff_time, int begin_hour, int end_hour, double lat,
			double lng, JDBCUtil jdbcUtils) {
		super();
		this.vin = vin;
		this.week = week;
		this.date = date;
		this.diff_time = diff_time;
		this.begin_hour = begin_hour;
		this.end_hour = end_hour;
		this.lat = lat;
		this.lng = lng;
		this.jdbcUtils = jdbcUtils;
	}

	@Override
	public void run() {
		try {
			GetCityName.getCity(vin, week, date, diff_time, begin_hour, end_hour, lat, lng, jdbcUtils);
		} catch (IOException e) {
			System.out.println(
					"VIN: " + vin + "  date: " + date + "  begin_hour: " + begin_hour + "  end_hour: " + end_hour);
			System.out.println("run: error:" + e.getMessage());
		}
	}

}