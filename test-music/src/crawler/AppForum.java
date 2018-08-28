package crawler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

public class AppForum extends App {

	public static String getURL(String base_url, int num) {
		if (num < 2)
			return base_url;
		int index = base_url.lastIndexOf("-");
		return base_url.substring(0, index) + "-" + num + base_url.substring(index + 2);
	}

	public static int getPages(Document doc) throws IOException {

		String tmp = doc.select("div.pagearea").select("span.fr").text();
		if (tmp == null || tmp.trim().length() < 1) {
			tmp = doc.select("div.pagearea").select("div.pages").attr("maxindex");
			if (tmp.trim().length() < 1) {
				tmp = doc.select("div.pagearea").select("div.fs").attr("title");
				if (tmp.trim().length() < 1)
					return 1;
			} else {
				return Integer.parseInt(tmp);
			}
		}
		Pattern pat = Pattern.compile("共\\s?([\\d]+)\\s?页");
		Matcher mat = pat.matcher(tmp.trim());
		mat.matches();

		return Integer.parseInt(mat.group(1));
	}

}
