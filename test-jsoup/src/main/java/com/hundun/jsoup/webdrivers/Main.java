package com.hundun.jsoup.webdrivers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

public class Main {

	public static void snapshot(TakesScreenshot drivername, String filename) {

		File scrFile = drivername.getScreenshotAs(OutputType.FILE);
		try {
			System.out.println("save snapshot path is:E:/" + filename);
			FileUtils.copyFile(scrFile, new File("./" + filename));
		} catch (IOException e) {
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		} finally {
			System.out.println("screen shot finished");
		}
	}

	public static void snapshot2(WebDriver drivername, String filename) {
		try {
			WebDriver augmentedDriver = new Augmenter().augment(drivername);
			File screenshot = ((TakesScreenshot) augmentedDriver)
					.getScreenshotAs(OutputType.FILE);
			File file = new File("./" + filename);
			FileUtils.copyFile(screenshot, file);
		} catch (IOException e) {
			System.out.println("Can't save screenshot");
			e.printStackTrace();
		} finally {
			System.out.println("screen shot finished");
		}
	}

	public static byte[] takeScreenshot(WebDriver driver) throws IOException {
		WebDriver augmentedDriver = new Augmenter().augment(driver);
		return ((TakesScreenshot) augmentedDriver)
				.getScreenshotAs(OutputType.BYTES);
	}

	public static BufferedImage createElementImage(WebDriver driver,
			WebElement webElement) throws IOException {
		Point location = webElement.getLocation();
		Dimension size = webElement.getSize();
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(
				takeScreenshot(driver)));
		BufferedImage croppedImage = originalImage.getSubimage(location.getX(),
				location.getY(), size.getWidth(), size.getHeight());
		return croppedImage;
	}

	public static void main(String[] args) {

		String URL = "http://blog.csdn.net/marksinoberg/article/details/58644436";// http://coral.qq.com/1008591939

		IDriver idriver = new FirefoxDriver();
		WebDriver driver = idriver.getDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		// driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		// driver.manage.script_timeout = 3 #seconds
		// driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
		// driver.navigate().to(URL);
		driver.get(URL);
		// max size the browser
		driver.manage().window().maximize();
		/* Navigation navigation = driver.navigate(); navigation.to(URL); */
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// snapshot((TakesScreenshot)driver,"open_baidu.png");
		snapshot2(driver, "./open_baidu_sinaweibo.png");

		driver.quit();

	}

}
