package com.hundun.flume;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @DESC
 * @author xinshiyou
 */
public class RPCFlume {

	static final String flumeHost = "flume-1.sv.dat.alsh.intra.im";
	static final int flumePort = 41413;

	public static void main(String[] args) {

		MyRpcClientFacade client = new MyRpcClientFacade();
		client.init(flumeHost, flumePort);

		String sampleData = "Hello Flume!";

		int count = 0;
		while (count++ < 10) {
			client.sendDataToFlume(sampleData + "123456123456789132456798"
					+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		client.cleanUp();
	}
}
