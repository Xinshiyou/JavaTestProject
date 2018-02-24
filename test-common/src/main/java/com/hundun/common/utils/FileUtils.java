package com.hundun.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @DESC 处理一般文本文件IO：text,csv等字符存储文本文件；外部处理异常
 * @author saic_xinshiyou
 */
public class FileUtils {

	private static Logger logger = Logger.getLogger(FileUtils.class);

	final static String fileSep = java.io.File.separator;
	final static String lineSep = System.getProperty("line.separator", "\n");
	final static String comma = ",";

	public static List<String> getLines(String fileName) throws IOException {

		File file = new File(fileName);
		if (!file.exists())
			return null;

		return getLines(file);
	}

	public static List<String> getLines(File file) throws IOException {

		FileInputStream fis = new FileInputStream(file);

		return getLines(fis);
	}

	public static List<String> getLines(InputStream in) throws IOException {

		BufferedReader br = getBufferedReader(in);

		List<String> res = new ArrayList<String>();
		String line = null;
		while (null != (line = br.readLine()))
			res.add(line);
		close(br);

		return res;
	}

	public static void write2FileLog(String filename, String cnt)
			throws IOException {
		File file = new File(filename);
		write2FileLog(file, cnt);
	}

	public static void write2FileLog(File file, String cnt) throws IOException {
		checkFile(file);
		BufferedWriter bw = getBufferedWriter(file);
		bw.write(cnt + lineSep);
		close(bw);
	}

	static void checkFile(File file) throws IOException {
		if (!file.exists())
			createFile(file.getAbsolutePath());
	}

	static BufferedWriter getBufferedWriter(File file)
			throws FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				file, true)));
	}

	static BufferedWriter getBufferedWriter(OutputStream out)
			throws FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(out));
	}

	static BufferedReader getBufferedReader(InputStream in)
			throws FileNotFoundException {
		return new BufferedReader(new InputStreamReader(in));
	}

	static BufferedReader getBufferedReader(File file)
			throws FileNotFoundException {
		FileInputStream in = new FileInputStream(file);
		return new BufferedReader(new InputStreamReader(in));
	}

	static void close(Writer w) throws IOException {
		w.flush();
		w.close();
	}

	static void close(Reader r) throws IOException {
		r.close();
	}

	public static void write2File(String filename, List<String> lst)
			throws IOException {
		File f = new File(filename);
		checkFile(f);
		write2File(f, lst);
	}

	public static void write2File(File f, List<String> lst) throws IOException {
		BufferedWriter bw = getBufferedWriter(f);
		for (String line : lst)
			bw.write(line + lineSep);
		close(bw);
	}

	/**
	 * @DESC 固定顺序输出文件
	 * @param out
	 * @param title字段名称放在首行
	 * @param lst
	 * @param sep字段之间的分割符
	 * @throws IOException
	 */
	public static void write2File(OutputStream out, String[] title,
			List<Map<String, Object>> lst, String sep) throws IOException {
		BufferedWriter bw = getBufferedWriter(out);
		StringBuffer sb = new StringBuffer();
		int len = title.length;
		for (int index = 0; index < len; index++)
			if (index < len - 1)
				sb.append(title[index] + sep);
			else
				sb.append(title[index]);
		sb.append(lineSep);
		for (Map<String, Object> map : lst) {
			for (int index = 0; index < len; index++)
				if (index < len - 1)
					sb.append(map.get(title[index]).toString() + sep);
				else
					sb.append(map.get(title[index]).toString());
			sb.append(lineSep);
		}
		bw.write(sb.toString());
		close(bw);
	}

	public static void write2CSV(String filename, List<Map<String, Object>> lst)
			throws Exception {

		File f = new File(filename);
		if (!f.exists())
			createFile(filename);
		FileOutputStream fos = new FileOutputStream(f);
		String[] title = new String[lst.get(0).keySet().size()];

		int index = 0;
		for (String key : lst.get(0).keySet())
			title[index++] = key;

		write2File(fos, title, lst, comma);

	}

	/**
	 * @DESC 写入文件，sep为分隔符
	 * @param filename
	 * @param lst
	 * @param sep
	 * @throws Exception
	 */
	public static void write2Text(String filename,
			List<Map<String, Object>> lst, String sep) throws Exception {
		File f = new File(filename);
		if (!f.exists())
			createFile(filename);
		FileOutputStream fos = new FileOutputStream(f);
		String[] title = new String[lst.get(0).keySet().size()];

		int index = 0;
		for (String key : lst.get(0).keySet())
			title[index++] = key;

		write2File(fos, title, lst, sep);
	}

	// 递归创建文件目录
	public static void createDir(String dirPath) {
		try {
			File f = new File(dirPath);
			if (!f.exists() && dirPath.length() > 2) {
				int idx = dirPath.lastIndexOf(fileSep);
				createDir(dirPath.substring(0, idx));
				f.mkdir();
			}
		} catch (Exception e) {// 出现异常，迭代下层文件
		}
	}

	public static void createFile(String filePath) {

		int idx = filePath.lastIndexOf(fileSep);
		String dirPath = filePath.substring(0, idx);
		if (!new File(dirPath).exists())
			createDir(dirPath);
		try {
			new File(filePath).createNewFile();
		} catch (IOException e) {
			logger.error(e);
		}
	}

}
