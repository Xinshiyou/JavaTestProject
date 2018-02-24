package com.hundun.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @DESC 获取WebDriver
 * @author saic_xinshiyou
 */
public class DriverUtils {

	public static final int WAIT_TIME = 60 * 1000;
	private static final Logger logger = LogManager.getLogger(DriverUtils.class);

	/**
	 * @DESC 获取PhantomJS
	 * @param phantomJS
	 * @return
	 */
	public static PhantomJSDriver getPhantomJs(String phantomJS) {
		return getPhantomJs(phantomJS, null, false, false);
	}

	/**
	 * @DESC 自定义User-Agent，获取PhantomJSDriver
	 * @param phantomJS
	 * @param userAgent
	 * @return
	 */
	public static PhantomJSDriver getPhantomJs(String phantomJS, String userAgent) {
		return getPhantomJs(phantomJS, userAgent, false, false);
	}

	/**
	 * @DESC 获取PhantomJSDriver
	 * @param phantomJS
	 * @param userAgent
	 * @param loadImages
	 * @return
	 */
	public static PhantomJSDriver getPhantomJs(String phantomJS, String userAgent, boolean loadImages) {
		return getPhantomJs(phantomJS, userAgent, loadImages, false);
	}

	/**
	 * @获取PhantomJSDriver
	 * @param phantomJS
	 * @param userAgent
	 * @param loadImages
	 * @param jsEnabled
	 * @return
	 */
	public static PhantomJSDriver getPhantomJs(String phantomJS, String userAgent, boolean loadImages,
			boolean jsEnabled) {
		return getPhantomJs(phantomJS, userAgent, loadImages, jsEnabled, "utf8");
	}

	/**
	 * @获取PhantomJSDriver
	 * @param phantomJS
	 * @param userAgent
	 * @param loadImages
	 * @param jsEnabled
	 * @return
	 */
	public static PhantomJSDriver getPhantomJs(String phantomJS, String userAgent, boolean loadImages,
			boolean jsEnabled, String encoding) {

		System.setProperty("phantomjs.binary.path", phantomJS);
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
		if (userAgent != null) {
			desiredCapabilities.setCapability("phantomjs.page.settings.userAgent", userAgent);
			desiredCapabilities.setCapability("phantomjs.page.customHeaders.User-Agent", userAgent);
		}
		desiredCapabilities.setJavascriptEnabled(jsEnabled);
		PhantomJSDriver driver = null;
		List<String> cli = new ArrayList<>();
		cli.add("--load-images=" + loadImages);
		cli.add("--output-encoding=" + encoding);
		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cli);
		driver = new PhantomJSDriver(desiredCapabilities);
		driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.MILLISECONDS);
		driver.manage().deleteAllCookies();

		return driver;
	}

	/**
	 * @DESC 获取HtmlUnitDriver
	 * @param userAgent
	 * @return
	 */
	public static HtmlUnitDriver getHtmlUnitDriver(String userAgent) {
		return getHtmlUnitDriver(userAgent, false, false);
	}

	/**
	 * @DESC 获取HtmlUnitDriver
	 * @param userAgent
	 * @param jsEnabled
	 * @return
	 */
	public static HtmlUnitDriver getHtmlUnitDriver(String userAgent, boolean jsEnabled) {
		return getHtmlUnitDriver(userAgent, jsEnabled, false);
	}

	/**
	 * @DESC 设置获取HtmlUnitDriver
	 * @param userAgent
	 * @param proxyEnabled
	 * @param jsEnabled
	 * @param loadImages
	 * @return
	 */
	public static HtmlUnitDriver getHtmlUnitDriver(String userAgent, boolean jsEnabled, boolean loadImages) {

		DesiredCapabilities desiredCapabilities = DesiredCapabilities.htmlUnit();
		desiredCapabilities.setCapability("phantomjs.page.settings.loadImages", false);
		desiredCapabilities.setJavascriptEnabled(jsEnabled);
		if (userAgent != null) {
			desiredCapabilities.setCapability("phantomjs.page.settings.userAgent", userAgent);
			desiredCapabilities.setCapability("phantomjs.page.customHeaders.User-Agent", userAgent);
		}
		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
				new String[] { "--load-images=" + loadImages });
		HtmlUnitDriver driver = null;
		driver = new HtmlUnitDriver(desiredCapabilities);
		driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.MILLISECONDS);
		driver.manage().deleteAllCookies();

		return driver;

	}

	/**
	 * @DESC 无密码IP代理
	 * @param userAgent
	 * @param proxyEnabled
	 * @param jsEnabled
	 * @param loadImages
	 * @return
	 */
	public static HtmlUnitDriver getHtmlUnitDriverProxyIP(String userAgent, boolean loadImages, String IP,
			String port) {

		DesiredCapabilities desiredCapabilities = DesiredCapabilities.htmlUnit();
		desiredCapabilities.setCapability("phantomjs.page.settings.loadImages", false);
		if (userAgent != null) {
			desiredCapabilities.setCapability("phantomjs.page.settings.userAgent", userAgent);
			desiredCapabilities.setCapability("phantomjs.page.customHeaders.User-Agent", userAgent);
		}
		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
				new String[] { "--load-images=" + loadImages });
		HtmlUnitDriver driver = null;
		Proxy proxy = new Proxy();
		proxy.setHttpProxy(IP + ":" + port);
		desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);
		driver = new HtmlUnitDriver(desiredCapabilities);
		driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.MILLISECONDS);
		driver.manage().deleteAllCookies();

		return driver;
	}

	public static void main(String[] args) {

		PhantomJSDriver driver = getPhantomJs("");
		driver.get("");
		CommandExecutor ce = driver.getCommandExecutor();
	}

}
