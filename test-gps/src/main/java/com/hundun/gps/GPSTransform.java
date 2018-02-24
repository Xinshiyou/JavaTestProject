package com.hundun.gps;

/**
 * @DESC 上海市经纬度转换为百度/Google地图坐标
 * @author saic_xinshiyou
 */
public class GPSTransform {

	public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	public static double pi = 3.14159265358979324;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;

	/**
	 * gg_lat 纬度 gg_lon 经度 GCJ-02转换BD-09 Google地图经纬度转百度地图经纬度
	 */
	public static double[] google_bd_encrypt(double gg_lat, double gg_lon) {
		double[] point = { 0, 0 };
		double x = gg_lon;
		double y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		point[0] = bd_lat;
		point[1] = bd_lon;
		return point;
	}

	/**
	 * wgLat 纬度 wgLon 经度 BD-09转换GCJ-02 百度转google
	 */
	public static double[] bd_google_encrypt(double bd_lat, double bd_lon) {
		double[] point = { 0, 0 };
		double x = bd_lon - 0.0065;
		double y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		point[0] = gg_lat;
		point[1] = gg_lon;
		return point;
	}

	/**
	 * wgLat 纬度 wgLon 经度 WGS-84 到 GCJ-02 的转换（即 GPS 加偏）
	 */
	public static double[] wgs_gcj_encrypts(double wgLat, double wgLon) {
		double[] point = { 0, 0 };
		if (outOfChina(wgLat, wgLon)) {
			point[0] = wgLat;
			point[1] = wgLon;
			return point;
		}

		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double lat = wgLat + dLat;
		double lon = wgLon + dLon;
		point[0] = lat;
		point[1] = lon;
		return point;
	}

	/** inner method for this class */
	private static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	private static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret = ret + (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret = ret + (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret = ret + (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret = ret + (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret = ret + (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret = ret + (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}

}