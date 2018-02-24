package com.hundun.jsoup.selogin;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.hundun.common.utils.DriverUtils;
import com.hundun.common.utils.FileUtils;

/**
 * @DESC 盖世汽车网，模拟登陆操作
 * @author saic_xinshiyou
 */
public class LoginMain {

	private static final String user = "st_0722010108@aliyun.com";
	private static final String pwd = "457005129gas";

	static final String url = "http://i.gasgoo.com/login.aspx?return=http://i.gasgoo.com/index.aspx";
	static final String phantomjs = "/Users/saic_xinshiyou/Downloads/phantomjs/bin/phantomjs";

	/**
	 * @DESC 说明：<br>
	 *       <ul>
	 *       <li>登录目的：获取登录时的Cookies信息，用于其他操作使用
	 *       <li>登录流程：
	 *       <ul>
	 *       <li>打开登录网页，进行用户名、密码输入
	 *       <li>触发相应的登录方法，触发JS操作
	 *       <li>登录之后，获取登录Cookies
	 *       </ul>
	 *       </ul>
	 */

	public static void main(String[] args) {

		/**
		 * 1、使用phantomjs或调用浏览器接口，必须使用能够运行JS的Driver；如果目标网站只需要提交表单就可以完成登录操作，
		 * 那么使用http就可以实现该功能<br>
		 * 2、等待登录完成，一般JS加载的话，等待几秒，或通过其他手段测试是否加载完成<br>
		 * 3、加载完成之后，获取Cookies
		 */

		/**
		 * 1、实际上如果自始至终，只需要使用同一个Driver的话，不需要获取Cookies<br>
		 * 2、获取Cookies的目的是能够使其他Driver也能够访问目标网站
		 */

		// 1、 生成Driver，加载登录网页
		PhantomJSDriver driver = DriverUtils.getPhantomJs(phantomjs);
		driver.get(url);

		// 2、 在输入框中输入用户名、用户密码
		driver.findElement(By.cssSelector("input#txtUserName")).sendKeys(user);
		driver.findElement(By.cssSelector("input#txtPassword")).sendKeys(pwd);

		// 3、 执行登录JS操作
		driver.executeScript("userAjaxLogin()");

		// 4、 等待登录过程加载完成
		try {// 此处仅为演示，后期可以不断优化，或通过其他手段来跳过sleep操作
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 其他操作-----BEGIN-----
		try {
			FileUtils.write2FileLog("./test.html", driver.getPageSource());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 其他操作-----END-----

		// 5、 Driver使用完成，切记需要手动关闭
		driver.quit();

	}

}
