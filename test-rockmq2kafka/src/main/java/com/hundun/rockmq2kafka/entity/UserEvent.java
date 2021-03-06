package com.hundun.rockmq2kafka.entity;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserEvent {

	private String ua = null; // string 浏览器user_agent
	private String utm = null; // string utm 字段
	private String user_id = null; // string userid，用户id (uid->user_id)
	private String uuid = null; // string 美团唯一移动设备标示
	private String channel = null; // string 客户端下载频道 (ch->channel)
	private String city_id = null; // int 用户所在城市id (cityid->city_id)
	private String client_type = null; // string 客户端类型 (ct->client_type)
	private String device_id = null; // string 设备id（android）
										// (deviceid->device_id)
	private String device_model = null; // string 设备型号 (dm->device_model)
	private String iccid = null; // string sim卡卡号
	private String idfa = null; // string 广告标识符(iOS)
	private String imsi = null; // string 国际移动用户识别码
	private String lat = null; // double 纬度
	private String lng = null; // double 经度
	private String lch = null; // string 客户端启动渠道
	private String client_ip = null; // string 客户端ip (后端解析)
	private String android_id = null; // string 安卓ID
	private String brand = null; // string 生产厂商
	private String apn = null; // string wifi 接入点名称列表
	private String app_version = null; // string 应用版本
	private String app_name = null; // string 应用名
	private String mac = null; // string 设备mac地址
	private String mno = null; // string 运营商
	private String bluetooth = null; // string 手机蓝牙模块状态 (bht->bluetooth)
	private String category = null; // string 业务名称(默认 xbl)
	private String idfv = null; // string idfv(iOS)
	private String key_chain_id = null; // string key_chain_id(iOS)
	private String locate_city_id = null; // bigint 定位城市 id
	private String openudid = null; // string openudid(iOS)
	private String sn = null; // string 安卓的 sn 号
	private String vendorid = null; // string 设备ID(ios6及以上系统)
	private String vendor_id = null; // string 设备ID(ios6及以上系统)
	private String version_code = null; // string version_code
	private String net = null; // string 网络接入方式
	private String wifi = null; // string 无线 wifi 模块状态
	private String os_version = null; // string 操作系统版本
	private String push_setting = null; // int Push 开关设置 (ps->push_setting)
	private String push_id = null; // string push_id (pushid->push_id)
	private String screen = null; // string 屏幕分辨率 (sc->screen)
	private String sdk_verion = null; // string sdk版本 (sdk_ver->sdk_verion)
	private String sub_channel_id = null; // string 子渠道id
											// (subcid->sub_channel_id)
	private String imci = null; // string 国际移动装备辨识码(android)
	private String wxid = null; // string 微信小程序SDK中用户账号唯一标识(H5)
	private String sdtk = null; // string APNS 使用的 PushToken (iOS)
	private String page_name = null;// string 页面名称 (val_cid->page_name)
	private String refer_page_name = null;// string 事件发生页面ref
											// (ref_val_cid->ref_page_name)
	private String req_id = null;// string session 内页面的请求 ID
	private String refer_req_id = null;// string 前一个页面请求 ID
	private String url = null;// string url(H5)
	private String referer_url = null;// string 上个页面 url(H5)
	private String session_id = null;// string session id (msid->session_id)
	private String element_id = null;// string 控件标识
	private String event_type = null;// string 监听的动作类型(click，view)
	private String tm = null;// string 上报时间
	private String isnative = null;// int 是否是native(js:0, native 1)
	private String seq = null;// bigint 事件序号
	private String log_type = null;// string 事件名(pv, mv, report) (nm->log_type)
	private String tag = null;// string Tag标记
	private String custom = null;// string 事件自定义属性
	private String metrics = null;// string prometheus指标
	private String poi_id = null;// string 商家 id
	private String create_by = null;
	private String update_by = null;
	private String log_category = null; // 区分日志类型：20（端到端），30（错误日志），10或者缺省（用户行为打点）
	private String error_key = null; // key 错误标识符
	private String error_tag = null; // tag
	private String token = null; // string token，token
	private String client_name = null;

	private String id = null;
	private String create_time = null;
	private String update_time = null;

	// private static final Gson gson = new Gson();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map convertBean(Object bean)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

	/** parser JSON Object to UserEvent */
	public static UserEvent parserObject(String text) {

		UserEvent result = new UserEvent();// gson.fromJson(text,
											// UserEvent.class);
		JsonObject jobj = new JsonParser().parse(text).getAsJsonArray().get(0).getAsJsonObject();
		for (Entry<String, JsonElement> item : jobj.entrySet()) {
			String key = item.getKey();
			result.setValue(key, item.getValue().getAsString());
		}
		return result;
	}

	private void setValue(String name, Object value) {

		try {
			Field field = getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(this, value);
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

	}

	/**
	 * parse string to class object
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("resources/test.txt"));
		String test = br.readLine();
		br.close();

		UserEvent ue = UserEvent.parserObject(test);
		System.out.println("uuid:" + ue.getUuid());
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getUtm() {
		return utm;
	}

	public void setUtm(String utm) {
		this.utm = utm;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLch() {
		return lch;
	}

	public void setLch(String lch) {
		this.lch = lch;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public String getAndroid_id() {
		return android_id;
	}

	public void setAndroid_id(String android_id) {
		this.android_id = android_id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMno() {
		return mno;
	}

	public void setMno(String mno) {
		this.mno = mno;
	}

	public String getBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(String bluetooth) {
		this.bluetooth = bluetooth;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIdfv() {
		return idfv;
	}

	public void setIdfv(String idfv) {
		this.idfv = idfv;
	}

	public String getKey_chain_id() {
		return key_chain_id;
	}

	public void setKey_chain_id(String key_chain_id) {
		this.key_chain_id = key_chain_id;
	}

	public String getLocate_city_id() {
		return locate_city_id;
	}

	public void setLocate_city_id(String locate_city_id) {
		this.locate_city_id = locate_city_id;
	}

	public String getOpenudid() {
		return openudid;
	}

	public void setOpenudid(String openudid) {
		this.openudid = openudid;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getVendorid() {
		return vendorid;
	}

	public void setVendorid(String vendorid) {
		this.vendorid = vendorid;
	}

	public String getVersion_code() {
		return version_code;
	}

	public void setVersion_code(String version_code) {
		this.version_code = version_code;
	}

	public String getNet() {
		return net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public String getOs_version() {
		return os_version;
	}

	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}

	public String getPush_setting() {
		return push_setting;
	}

	public void setPush_setting(String push_setting) {
		this.push_setting = push_setting;
	}

	public String getPush_id() {
		return push_id;
	}

	public void setPush_id(String push_id) {
		this.push_id = push_id;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getSdk_verion() {
		return sdk_verion;
	}

	public void setSdk_verion(String sdk_verion) {
		this.sdk_verion = sdk_verion;
	}

	public String getSub_channel_id() {
		return sub_channel_id;
	}

	public void setSub_channel_id(String sub_channel_id) {
		this.sub_channel_id = sub_channel_id;
	}

	public String getImci() {
		return imci;
	}

	public void setImci(String imci) {
		this.imci = imci;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String getSdtk() {
		return sdtk;
	}

	public void setSdtk(String sdtk) {
		this.sdtk = sdtk;
	}

	public String getPage_name() {
		return page_name;
	}

	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}

	public String getRefer_page_name() {
		return refer_page_name;
	}

	public void setRefer_page_name(String refer_page_name) {
		this.refer_page_name = refer_page_name;
	}

	public String getReq_id() {
		return req_id;
	}

	public void setReq_id(String req_id) {
		this.req_id = req_id;
	}

	public String getRefer_req_id() {
		return refer_req_id;
	}

	public void setRefer_req_id(String refer_req_id) {
		this.refer_req_id = refer_req_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReferer_url() {
		return referer_url;
	}

	public void setReferer_url(String referer_url) {
		this.referer_url = referer_url;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getElement_id() {
		return element_id;
	}

	public void setElement_id(String element_id) {
		this.element_id = element_id;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public String getTm() {
		return tm;
	}

	public void setTm(String tm) {
		this.tm = tm;
	}

	public String getIsnative() {
		return isnative;
	}

	public void setIsnative(String isnative) {
		this.isnative = isnative;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getLog_type() {
		return log_type;
	}

	public void setLog_type(String log_type) {
		this.log_type = log_type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	public String getPoi_id() {
		return poi_id;
	}

	public void setPoi_id(String poi_id) {
		this.poi_id = poi_id;
	}

	public String getCreate_by() {
		return create_by;
	}

	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public String getLog_category() {
		return log_category;
	}

	public void setLog_category(String log_category) {
		this.log_category = log_category;
	}

	public String getError_key() {
		return error_key;
	}

	public void setError_key(String error_key) {
		this.error_key = error_key;
	}

	public String getError_tag() {
		return error_tag;
	}

	public void setError_tag(String error_tag) {
		this.error_tag = error_tag;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

}
