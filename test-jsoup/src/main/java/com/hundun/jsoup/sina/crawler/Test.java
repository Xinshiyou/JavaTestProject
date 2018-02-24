package com.hundun.jsoup.sina.crawler;

import java.io.IOException;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.google.gson.JsonParser;
import com.hundun.common.utils.DriverUtils;
import com.hundun.common.utils.FileUtils;

/**
 * @DESC 方法可行
 * @author saic_xinshiyou
 */
public class Test {

	protected static String phantomJSDriver = "/Users/saic_xinshiyou/Downloads/phantomjs-2.1.1-macosx/bin/phantomjs";
	protected static String prefix_url = "http://s.weibo.com/ajax/direct/morethan140?";
	protected static Logger logger = Logger.getLogger(Test.class);

	public static void main(String[] args) throws HttpException, IOException, InterruptedException {
		loadJS();
	}

	static void loadJS() throws HttpException, IOException, InterruptedException {

		PhantomJSDriver driver = DriverUtils.getPhantomJs(phantomJSDriver,
				"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0", false, true);
		SinaLogin sd = new SinaLogin();
		sd.init();
		HttpClient hc = sd.logonAndValidate("st_0722010108@aliyun.com", "457005129sina");
		PostMethod crawl = new PostMethod("http://s.weibo.com/weibo/%25E8%258D%25A3%25E5%25A8%2581&b=1&page=1");
		hc.executeMethod(crawl);
		crawl.releaseConnection();
		Cookie[] cks = hc.getState().getCookies();
		for (Cookie ck : cks)
			try {
				driver.manage().addCookie(new org.openqa.selenium.Cookie(ck.getName(), ck.getValue(), ck.getDomain(),
						ck.getPath(), ck.getExpiryDate(), ck.getSecure()));
			} catch (Exception e) {
			}

		driver.get("http://s.weibo.com/weibo/%25E8%258D%25A3%25E5%25A8%2581&b=1&page=1");
		Document doc = Jsoup.parse(driver.getPageSource());
		Elements eles = doc.select("div.WB_cardwrap.S_bg2.clearfix");
		System.out.println("Size: " + eles.size());
		catchContent(cks, eles);

		driver.quit();
	}

	static void catchContent(Cookie[] cks, Elements eles) {

		for (int i = 0; i < eles.size(); i++) {

			Element ele = eles.get(i);
			Entity ent = new Entity();

			try {
				ent.name = ele.select("div.feed_content.wbcon a.W_texta.W_fb").get(0).text();
				ent.date = ele.select("div.feed_from.W_textb a.W_textb").get(0).text().trim();

				Elements es = ele.select("ul.feed_action_info.feed_action_row4 li");
				try {
					ent.collected = es.get(0).select("span.line.S_line1 em").get(0).text().trim();
				} catch (Exception e) {
				}
				try {
					ent.transNums = es.get(1).select("span.line.S_line1 em").get(0).text().trim();
				} catch (Exception e) {
				}
				try {
					ent.cmntsNums = es.get(2).select("span.line.S_line1 em").get(0).text().trim();
				} catch (Exception e) {
				}
				try {
					ent.thumbsNums = es.get(3).select("span.line.S_line1 em").get(0).text().trim();
				} catch (Exception e) {
				}

				try {
					String commenturl = prefix_url + ele.select("a.WB_text_opt").get(0).attr("action-data");
					System.out.println("CURL: " + commenturl);
					ent.comment = getComment(cks, commenturl);
				} catch (Exception e) {
					ent.comment = ele.select("p.comment_txt").get(0).text().trim();
				}

				System.out.println("CNT: " + ent);
			} catch (Exception e) {
				System.out.println(ele);
			}
		}
	}

	static String getComment(Cookie[] cks, String url) throws HttpException, IOException {

		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpClient client = new HttpClient(connectionManager);

		PostMethod crawl = new PostMethod(url);

		for (Cookie item : cks)
			crawl.setRequestHeader(item.getName(), item.getValue());

		client.executeMethod(crawl);
		String content = crawl.getResponseBodyAsString();
		crawl.releaseConnection();

		FileUtils.write2FileLog("./test_" + url.substring(url.length() - 5), content);

		String html = new JsonParser().parse(content).getAsJsonObject().get("data").getAsJsonObject().get("html")
				.getAsString();
		return Jsoup.parse("<html><body>" + html + "</body></html>").select("body").get(0).text();
	}

}
