package com.saic.simclick;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.saic.AppForum;

public class Main {

	private static final ExecutorService es = Executors.newFixedThreadPool(1);
	private static final String FORMATED_TEXT = "title=[%s]\tdate=[%s]\treadNum=[%s]\tcommentNum=[%s]\tURL=[%s]";
	private static final Logger logger = Logger.getLogger(Main.class);
	private static String pageReg;

	/** @desc 执行将任务添加到Executor中 */
	private static class TimerTaskCls extends TimerTask {

		public TimerTaskCls() {
		}

		@Override
		public void run() {

			try {
				Thread.sleep(Math.round(Math.random() * 1000 + 500));
			} catch (InterruptedException e) {
				logger.info(e.getMessage());
				System.out.println(e.getMessage());
			}

			int pages = (int) Math.round(Math.random() * 2 + 3);
			List<String> lst = new ArrayList<>();
			for (int i = 0; i < pages; i++) {
				String url = String.format(pageReg, (i + 1) + "");
				lst.add(url);
			}

			Collections.shuffle(lst);
			List<String> target = new ArrayList<>();
			lst.forEach(url -> {
				List<String> result = null;
				try {
					result = parserPages(url);
				} catch (MalformedURLException e) {
				}
				int size = result.size();
				int numbers = (int) Math.floor(Math.random() * size);
				numbers = Math.min(numbers, size > 10 ? 10 : size);
				result.subList(0, numbers).forEach(item -> {
					target.add(item);
				});
			});

			Collections.shuffle(target);
			target.forEach(item -> {
				try {
					Thread.sleep((int) Math.floor(Math.random() * 1000));
				} catch (InterruptedException e) {
				}
				es.submit(new ClickRunner(item));
			});

			new Timer().schedule(new TimerTaskCls(), 30 * 60 * 1000 + (Math.round(Math.random() * 30 * 60)) * 1000);
		}

		private List<String> parserPages(String url) throws MalformedURLException {

			List<String> result = new ArrayList<String>();
			Document doc = getDocumentJS(url);
			Elements eles = doc.select("div.article-list div.article-item-box.csdn-tracking-statistics");
			System.out.println("Size:" + eles.size() + "\tURL:" + url);
			eles.forEach(item -> {

				String childURL = item.select("h4 a").attr("href");
				String title = item.select("h4 a").text();
				String date = item.select("div p span.date").text();

				Elements tmp = item.select("div p span.read-num");
				String readNum = tmp.get(0).text();
				String commentNum = tmp.get(1).text();

				result.add(childURL);

				System.out.println("URL:" + url + "\t\tInfos:"
						+ String.format(FORMATED_TEXT, title, date, readNum, commentNum, childURL));
			});

			return result;
		}
	}

	/**
	 * @desc 执行点击页面功能
	 * @author xinshiyou
	 */
	private static class ClickRunner implements Runnable {

		private String url;

		private ClickRunner(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			getDocumentJS(url);
			try {
				Thread.sleep(Math.round(Math.random() * 1000 + 1000));
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	/**
	 * @desc main method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args.length < 1) {
			logger.info("Less parameters, please input < page reg string >");
			System.out.println("Less parameters, please input < page reg string>");
			System.exit(1);
		}

		System.setProperty("webdriver.chrome.driver", "/root/chrome/chromedriver");
		pageReg = args[0];
		new Timer().schedule(new TimerTaskCls(), 1000);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (null != es)
					es.shutdown();
			}
		});

		while (true) {
			int threadCount = ((ThreadPoolExecutor) es).getActiveCount();
			System.out.println("Thread : active's thread size is :" + threadCount);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

	}

	/**
	 * @desc get chrome driver
	 */
	public static ChromeDriver getDriver() {

		HashMap<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_settings", 2);

		String userAgent = AppForum.getAgents();
		ChromeOptions options = new ChromeOptions();
		options.setBinary("/usr/bin/google-chrome-stable");
		options.setExperimentalOption("prefs", prefs);
		options.addArguments("--user-agent=" + userAgent);
		options.addArguments("--no-sandbox");
		options.addArguments("--test-type");
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		options.addArguments("--enable-strict-powerful-feature-restrictions");

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setJavascriptEnabled(true);

		ChromeDriver driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);

		return driver;
	}

	/**
	 * @desc crawler html content
	 * @param url
	 */
	public static Document getDocumentJS(String url) {

		ChromeDriver driver = getDriver();
		Document doc = null;
		try {
			driver.get(url);
			int length = (int) (Math.round(Math.random() * 100) + 100);
			driver.executeScript("scrollTo(0," + length + ")");
			System.out.println("URL:" + url + "\t\tTitle:" + driver.getTitle());
			doc = Jsoup.parse(driver.getPageSource());
		} catch (Exception e) {
		} finally {
			try {
				driver.quit();
				driver.close();
			} catch (Exception e) {
			}
		}

		return doc;
	}

}
