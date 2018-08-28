
package crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

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
		
		System.out.println("begin to create driver");

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
		
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setJavascriptEnabled(true);

		ChromeDriver driver = new ChromeDriver(capabilities);
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);

		// ChromeDriver driver = new ChromeDriver(options);

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
			System.out.println("begin to login");
			login(driver);
			System.out.println("login success");
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
		
		System.out.println("Begin to get login url");		

		driver.get("https://music.163.com/#/login");
		try{
		FileUtils.writeStringToFile(new File("temp.txt"),driver.getPageSource(),"UTF-8",false);
		}catch(IOException e){
			System.out.println("Exception:"+e.getMessage());
		}
		
		System.out.println("End of get login url");
		
		System.out.println("Begin to login");
		driver.findElementById("ntp-login-netease").findElement(By.id("e")).sendKeys(username);
		driver.findElementById("ntp-login-netease").findElement(By.id("epw")).sendKeys(password);
		driver.findElementById("ntp-login-netease").findElement(By.cssSelector("a.js-primary.u-btn2.u-btn2-2")).click();
		
		System.out.println("End of login");

	}

}
