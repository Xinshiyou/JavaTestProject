package crawler;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * @DESC 提供一些全局变量、静态方法
 * @author saic_xinshiyou
 */
public class App {

	final public static long ACCESS_PAGE_PAUSE_TIME_MILLISECONDS = 200;
	final public static long WAIT_TIME_FOR_THREAD_POOL_PROCESS_MILLISECONDS = 20000;
	final public static long WAIT_TIME_FOR_THREAD_POOL_TERMINATE_SECONDS = 3600;
	final public static long WAIT_TIMEOUT_SECONDS = 2 * 60;
	final public static long WAIT_TRY_AGAIN = 100;

	public static HtmlUnitDriver getHtmlUnitDriver() {
		return null;
	}

	public static String getAgents() {

		String[] user_agents = new String[] {
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; it; rv:1.8.1.11) Gecko/20071127 Firefox/2.0.0.11",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
				"Mozilla/5.0 (compatible; Konqueror/3.5; Linux) KHTML/3.5.5 (like Gecko) (Kubuntu)",
				"Mozilla/5.0(iPad;U;CPUOS4_3_3likeMacOSX;en-us)AppleWebKit/533.17.9(KHTML,likeGecko)Version/5.0.2Mobile/8J2Safari/6533.18.5",
				"Mozilla/4.0 (compatible;MSIE7.0;WindowsNT5.1;Trident/4.0;SE2.XMetaSr1.0;SE2.XMetaSr1.0;.NETCLR2.0.50727;SE2.XMetaSr1.0)",
				"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.0.12) Gecko/20070731 Ubuntu/dapper-security Firefox/1.5.0.12",
				"Mozilla/5.0 (X11; Linux i686) AppleWebKit/535.7 (KHTML, like Gecko) Ubuntu/11.04 Chromium/16.0.912.77 Chrome/16.0.912.77 Safari/535.7",
				"Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:10.0) Gecko/20100101 Firefox/10.0 ",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36" };

		return user_agents[(int) Math.floor(Math.random() * user_agents.length)];
	}

}
