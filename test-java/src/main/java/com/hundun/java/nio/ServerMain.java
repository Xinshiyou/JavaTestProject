package com.hundun.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class ServerMain {

	private static final int ECHO_SERVER_PORT = 6789;
	private static final int ECHO_SERVER_TIMEOUT = 5000;
	private static final int BUFFER_SIZE = 1024;
	private static ServerSocketChannel serverChannel = null;
	private static Selector selector = null; // 多路复用选择器
	private static ByteBuffer buffer = null; // 缓冲区

	public static void main(String[] args) {
		init();
		listen();
	}

	private static void init() {
		try {
			serverChannel = ServerSocketChannel.open();
			buffer = ByteBuffer.allocate(BUFFER_SIZE);
			serverChannel.socket()
					.bind(new InetSocketAddress(ECHO_SERVER_PORT));
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void listen() {
		while (true) {
			try {
				if (selector.select(ECHO_SERVER_TIMEOUT) != 0) {
					Iterator<SelectionKey> it = selector.selectedKeys()
							.iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						handleKey(key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void handleKey(SelectionKey key) throws IOException {
		SocketChannel channel = null;
		Charset charset = Charset.forName("UTF-8");
		try {
			if (key.isAcceptable()) {
				ServerSocketChannel serverChannel = (ServerSocketChannel) key
						.channel();
				channel = serverChannel.accept();
				channel.configureBlocking(false);
				channel.register(selector, SelectionKey.OP_READ);
			} else if (key.isReadable()) {
				channel = (SocketChannel) key.channel();
				buffer.clear();
				if (channel.read(buffer) > 0) {
					buffer.flip();
					CharBuffer charBuffer = charset.decode(buffer);
					String msg = charBuffer.toString();
					System.out.println("收到" + channel.getRemoteAddress()
							+ "的消息：" + msg);
					channel.write(charset.encode(charBuffer));
				} else {
					channel.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (channel != null) {
				channel.close();
			}
		}
	}

}
