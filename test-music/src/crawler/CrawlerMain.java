
package crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CrawlerMain {

	/**
	 * @desc main method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		System.setProperty("webdriver.chrome.driver", "/root/chrome/chromedriver");
		Document document = getDocumentJS("https://music.163.com/#/my");
		System.out.println("Docuemnt:\n" + document);
		FileUtils.writeStringToFile(new File("./data.txt"), document.toString(), "UTF-8", false);
	}

	/**
	 * @desc get chrome driver
	 */
	public static ChromeDriver getDriver() {

		HashMap<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_settings", 2);
		prefs.put("profile.default_content_setting_values", 2);
		prefs.put("profile.managed_default_content_settings.images", 2);

		String userAgent = AppForum.getAgents();
		ChromeOptions options = new ChromeOptions();
		options.setBinary("/usr/bin/google-chrome-stable");
		options.setExperimentalOption("prefs", prefs);
		options.addArguments("--user-agent=" + userAgent);
		options.addArguments("--no-sandbox", "--test-type");
		options.addArguments("--disable-infobars", "--headless", "--disable-gpu");
		options.addArguments("--enable-strict-powerful-feature-restrictions");
		options.addArguments("--disable-plugins", "--disable-images", "--start-maximized");

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setCapability(ChromeOptions.CAPABILITY, options);
		chromeOptions.setCapability("javascriptEnabled", true);

		ChromeDriver driver = new ChromeDriver(chromeOptions);

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
			login(driver);
			driver.get(url);
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

	private static void login(ChromeDriver driver) {

		final String username = "username";
		final String password = "password";

		driver.get("https://music.163.com/#/login");
		driver.findElementById("ntp-login-netease").findElement(By.id("e")).sendKeys(username);
		driver.findElementById("ntp-login-netease").findElement(By.id("epw")).sendKeys(password);
		driver.findElementById("ntp-login-netease").findElement(By.cssSelector("a.js-primary.u-btn2.u-btn2-2")).click();

	}

}
