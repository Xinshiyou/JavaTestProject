package com.hundun.jsoup.crawlpng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @DESC 获取图片素材：车牌识别
 * @author saic_xinshiyou
 */
public class CrawlMain {

	/**
	 * @DESC crawler some materials by JSOUP : simple
	 */

	public static void main(String[] args) {

		String url = "http://www.oinbag.com/";
		ExecutorService es = Executors.newFixedThreadPool(10);
		while (true) {

			Elements eles = null;
			try {
				Document doc = Jsoup.connect(url).timeout(10 * 60 * 1000).get();
				eles = doc.select("#event-list div.event-container");
			} catch (IOException e1) {
				System.out.println("Error: " + e1.getMessage());
			}

			for (Element ele : eles) {
				String name = ele.select("a.car-no").get(0).attr("title").trim();
				Elements tmp = ele.select("ul li.event-img-file");
				for (Element temp : tmp) {
					String temp_ur = temp.select("a").get(0).attr("href");
					es.submit(new DownloadRunner(name, temp_ur));
				}
			}

			try {
				Thread.sleep(5 * 60 * 1000);
			} catch (InterruptedException e) {
			}
		}

	}

	static class DownloadRunner implements Runnable {

		private String name = null;
		private String url = null;

		public DownloadRunner(String name, String url) {
			this.name = name;
			this.url = url;
		}

		@Override
		public void run() {

			File f = new File("./data");
			if (!f.exists())
				f.mkdir();

			f = new File("./data/" + name);
			if (!f.exists())
				f.mkdir();

			download();
		}

		private void download() {
			byte[] image = getImageFromNetByUrl(url);
			writeImageToDisk(image, "./data/" + name);
		}

		/**
		 * @desc 将图片写入到磁盘
		 * @param img
		 * @param fileName
		 */
		public static void writeImageToDisk(byte[] img, String fileName) {
			try {
				File file = new File("C:\\" + fileName);
				FileOutputStream fops = new FileOutputStream(file);
				fops.write(img);
				fops.flush();
				fops.close();
				System.out.println("图片已经写入到C盘");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * @desc 根据地址获得数据的字节流
		 * @param strUrl
		 */
		public static byte[] getImageFromNetByUrl(String strUrl) {
			try {
				URL url = new URL(strUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
				byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
				return btImg;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * @desc 从输入流中获取数据
		 * @param inStream
		 * @throws Exception
		 */
		public static byte[] readInputStream(InputStream inStream) throws Exception {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			inStream.close();
			return outStream.toByteArray();
		}

	}

}
