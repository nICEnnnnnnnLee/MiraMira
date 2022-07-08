package top.nicelee.mirai.miramira.app.txt2gif.libs.gif;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImgUtil {

	/**
	 * 裁剪图片
	 * 
	 * @param img
	 * @param offsetX
	 * @param offsetY
	 * @param width
	 * @param height
	 * @return 裁剪后的图片
	 */
	public static BufferedImage cut(BufferedImage img, int offsetX, int offsetY, int width, int height) {
		BufferedImage img0 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img0.getGraphics();
		g.drawImage(img, 0, 0, width, height, offsetX, offsetY, offsetX + width, offsetY + height, null);
		g.dispose();
		return img0;
	}

	/**
	 * 缩放/拉伸图片
	 * 
	 * @param img    待处理图片
	 * @param width
	 * @param height
	 * @return 处理后的图片
	 */
	public static BufferedImage resize(BufferedImage img, int width, int height) {
		Image scaled = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		// 创建一个新的画布，在上面画裁剪后的图
		BufferedImage img0 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img0.getGraphics();
		g.drawImage(scaled, 0, 0, null);
		g.dispose();
		return img0;
	}

	/**
	 * 给图片添加文字(原图片会有改动)
	 * 
	 * @param img
	 * @param text
	 * @param offsetX
	 * @param OffsetY
	 * @param font
	 * @param color
	 * @return 处理后的图片
	 */
	public static BufferedImage addText(BufferedImage img, String text, int offsetX, int OffsetY, Font font,
			Color color) {
		return addText(img, text, offsetX, OffsetY, font, color, null);
	}

	/**
	 * 给图片添加文字(原图片会有改动)
	 * 
	 * @param img
	 * @param text
	 * @param offsetX
	 * @param OffsetY
	 * @param font
	 * @param color
	 * @param colorShadow 阴影颜色
	 * @return 处理后的图片
	 */
	public static BufferedImage addText(BufferedImage img, String text, int offsetX, int OffsetY, Font font,
			Color color, Color colorShadow) {
		Graphics g = img.getGraphics();
		g.setFont(font);
		if (colorShadow != null) {
			g.setColor(colorShadow);
			g.drawString(text, offsetX + 1, OffsetY + 1);
		}
		g.setColor(color);
		g.drawString(text, offsetX, OffsetY);
		g.dispose();
		return img;
	}

	/**
	 * 给图片添加文字(原图片会有改动)
	 * 
	 * @param img
	 * @param text
	 * @param offsetX
	 * @param OffsetY
	 * @return 处理后的图片
	 */
	public static BufferedImage addText(BufferedImage img, String text, int offsetX, int OffsetY) {
		Graphics g = img.getGraphics();
		g.drawString(text, offsetX, OffsetY);
		g.dispose();
		return img;
	}

	/**
	 * 计算文字居中所占的偏移
	 * 
	 * @param imgWidth
	 * @param text
	 * @param font
	 * @return
	 */
	public static int offsetXCenter(int imgWidth, String text, Font font) {
		int offset = (imgWidth - text.length() * font.getSize()) / 2;
		return offset;
	}

	public static void main(String[] a) {
		try {
			BufferedImage img = (BufferedImage) ImageIO.read(new File("test.png"));
			img = cut(img, 100, 100, 500, 400);
			ImageIO.write(img, "jpg", new File("cut.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
