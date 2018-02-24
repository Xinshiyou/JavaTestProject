package com.hundun.jsoup.webdrivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @DESC fire fox web browser
 * @author saic_xinshiyou
 */
public class FirefoxDriver extends IDriver {

	@Override
	public WebDriver getDriver() {

		DesiredCapabilities capability = DesiredCapabilities.firefox();
		System.setProperty("webdriver.firefox.bin",
				"/Applications/Firefox.app/Contents/MacOS/firefox");
		capability.setJavascriptEnabled(true);
		return new org.openqa.selenium.firefox.FirefoxDriver(capability);
	}
}
