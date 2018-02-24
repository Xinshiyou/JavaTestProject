package com.hundun.scp;

import java.io.File;
import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

public class SCPMain {

	private String USER = null;// 登录用户名
	private String PASSWORD = null;// 生成私钥的密码和登录密码，这两个共用这个密码
	private Connection connection = null;
	private int isAuth = -1;

	public SCPMain(String ip, String port, String user, String password)
			throws IOException {
		this.USER = user;
		this.PASSWORD = password;

		connection = new Connection(ip, Integer.parseInt(port));
	}

	/**
	 * ssh用户登录验证，使用用户名和密码来认证
	 * 
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean isAuthedWithPassword(String user, String password) {
		try {
			return connection.authenticateWithPassword(user, password);
		} catch (IOException e) {
		}
		return false;
	}

	/**
	 * @DESC ssh用户登录验证，使用用户名、私钥、密码来认证 其中密码如果没有可以为null，生成私钥的时候如果没有输入密码，则密码参数为null
	 * @param user
	 * @param privateKey
	 * @param password
	 * @return
	 */
	public boolean isAuthedWithPublicKey(String user, File privateKey,
			String password) {
		try {
			return connection.authenticateWithPublicKey(user, privateKey,
					password);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isAuth() {
		return isAuthedWithPassword(USER, PASSWORD);
	}

	public void getFile(String remoteFile, String path) {
		try {
			connection.connect();
			boolean isAuthed = isAuth();
			if (isAuthed) {
				System.out.println("认证成功!");
				SCPClient scpClient = connection.createSCPClient();
				scpClient.get(remoteFile, path);
			} else {
				System.out.println("认证失败!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void putFile(String localFile, String remoteTargetDirectory) {

		Session sess = null;
		try {
			if (isAuth < 0) {
				connection.connect();
				isAuth = isAuth() ? 1 : 0;
			}
			if (null == sess)
				sess = connection.openSession();
			System.out.println("LocalFile: " + localFile + "\t\tAuth: "
					+ isAuth + "\t\tSess: " + (null == sess));

			if (isAuth > 0) {

				if (remoteTargetDirectory.endsWith("/"))
					remoteTargetDirectory = remoteTargetDirectory.substring(0,
							remoteTargetDirectory.length() - 1);

				sess.execCommand("mkdir -p " + remoteTargetDirectory);
				System.out.println("remoteDir: " + remoteTargetDirectory);

				SCPClient scpClient = connection.createSCPClient();
				File f = new File(localFile);
				if (!f.exists()) {
					System.out.println("文件不存在：" + localFile);
					return;
				}

				if (f.isFile()) {
					scpClient.put(localFile, remoteTargetDirectory);
				} else if (f.isDirectory()) {
					for (File name : f.listFiles()) {
						System.out.println("Filename : "
								+ name.getAbsolutePath());
						if (name.isFile())
							scpClient.put(name.getAbsolutePath(),
									remoteTargetDirectory);
						else if (name.isDirectory()) {
							System.out.println("File Dir: "
									+ name.getAbsolutePath() + "\t"
									+ remoteTargetDirectory + "/"
									+ name.getName());
							putFile(name.getAbsolutePath(),
									remoteTargetDirectory + "/"
											+ name.getName());
						} else
							System.out.println("123456--不是文件，也不是文件夹: "
									+ name.getName());
					}
				} else
					System.out.println("不是文件，也不是文件夹: " + localFile);
			} else
				System.out.println("认证失败!");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != sess)
				sess.close();
			sess = null;
		}
	}

	public static void printMsg() {

		print("==========");
		print("Usage:");
		print("Input parameters: ip\tport\tuse\tpassword\tsrcDir or file \t targetDir");
		print("==========");

	}

	public static void print(String msg) {
		System.out.println(msg);
	}

	public void close() {
		if (null != connection)
			connection.close();
	}

	public static void main(String[] args) throws Exception {

		printMsg();
		if (args.length < 6)
			throw new Exception("Less parameters");

		String ip = args[0];
		String port = args[1];
		String userName = args[2];
		String passWord = args[3];
		String src = args[4];
		String target = args[5];

		SCPMain main = null;
		try {
			main = new SCPMain(ip, port, userName, passWord);
			main.putFile(src, target);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			main.close();
		}

	}

}
