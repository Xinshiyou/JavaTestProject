package com.hundun.jsoup.webdrivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @DESC phantomjs driver
 * @author saic_xinshiyou
 */
public class PhantomjsDriver extends IDriver {

	@Override
	public WebDriver getDriver() {
		DesiredCapabilities capability = DesiredCapabilities.phantomjs();
		capability.setJavascriptEnabled(true);
		System.setProperty("phantomjs.binary.path",
				"/Users/saic_xinshiyou/Downloads/phantomjs-2.1.1-macosx/bin/phantomjs");
		return new PhantomJSDriver(capability);
	}
}
