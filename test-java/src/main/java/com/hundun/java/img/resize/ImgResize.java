package com.hundun.java.img.resize;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImgResize {

	private static int WIDTH = -1;
	private static int HEIGHT = -1;

	/**
	 * @DESC 改变图片的大小到宽为size，然后高随着宽等比例变化
	 * @param is:上传的图片的输入流
	 * @param os:改变了图片的大小后，把图片的流输出到目标OutputStream
	 * @param size:新图片的宽
	 * @param format:新图片的格式
	 * @throws IOException
	 */
	public static OutputStream resizeImage(InputStream is, OutputStream os, String format) throws IOException {
		BufferedImage prevImage = ImageIO.read(is);
		int newWidth = ImgResize.WIDTH;
		int newHeight = ImgResize.HEIGHT;
		BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
		Graphics graphics = image.createGraphics();
		graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
		ImageIO.write(image, format, os);
		os.flush();
		is.close();
		os.close();

		return os;
	}

	public static void main(String[] args) {

		if (args.length < 4) {
			System.out.println("Less parameters: src image, dst img path, dst img width, dst img height");
			return;
		}

		String srcImg = args[0];// resource: input src image
		String dstPath = args[1];// target directory
		String dstW = args[2];// target width
		String dstH = args[3];// target height

		ImgResize.WIDTH = Integer.parseInt(dstW);
		ImgResize.HEIGHT = Integer.parseInt(dstH);
		try {
			InputStream is = new FileInputStream(new File(srcImg));
			OutputStream os = new FileOutputStream(new File(dstPath));
			resizeImage(is, os, "png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
