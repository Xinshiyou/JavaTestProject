package com.hundun.jsoup.webdrivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ChromeDriver extends IDriver {

	@Override
	public WebDriver getDriver() {
		DesiredCapabilities capability = DesiredCapabilities.firefox();
		System.setProperty("webdriver.chrome.driver",
				"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
		capability.setJavascriptEnabled(true);
		return new org.openqa.selenium.chrome.ChromeDriver(capability);
	}

}
