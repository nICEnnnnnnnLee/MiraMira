package top.nicelee.mirai.miramira.app.gif;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import top.nicelee.mirai.miramira.app.gif.bean.TextOption;
import top.nicelee.mirai.miramira.app.gif.encoder.GIFEncoder;

public class GifUtil {

	/**
	 * 按帧提取将gif所有图片，并保存到dstfolder文件夹
	 * 
	 * @param gif       gif文件
	 * @param dstfolder 目标文件夹
	 * @return 裁剪后的图片
	 */
	public static boolean split(File gif, File dstfolder) {
		if (!dstfolder.exists())
			dstfolder.mkdirs();
		try {
			ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
			ImageInputStream ciis = ImageIO.createImageInputStream(new FileInputStream(gif));
			reader.setInput(ciis, true);
			try {
				for (int i = 0;; i++) {
					BufferedImage image = reader.read(i);
					ImageIO.write(image, "jpg", new File(dstfolder, String.format("%03d.jpg", i)));
				}
			} catch (IndexOutOfBoundsException e) {
			}
			ciis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将静态图片列表以恒定帧率的方式合成gif
	 * 
	 * @param imgs      图片列表
	 * @param gif       gif保存路径
	 * @param frameRate 帧率
	 */
	public static void merge(List<BufferedImage> imgs, String gif, int frameRate) {
		GIFEncoder encoder = new GIFEncoder();
		encoder.init(imgs.get(0));
		encoder.setFrameRate(frameRate);
		encoder.start(gif);
		for (int i = 1; i < imgs.size(); i++) {
			encoder.addFrame(imgs.get(i));
		}
		encoder.finish();
	}

	/**
	 * 给gif添加文字
	 * 
	 * @param source
	 * @param dest
	 * @param options
	 * @param frameRate 帧率
	 */
	public static void addText(InputStream source, OutputStream dest, List<TextOption> options, int frameRate) {
		try {
			ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
			ImageInputStream ciis = ImageIO.createImageInputStream(source);
			reader.setInput(ciis, true);
			GIFEncoder encoder = new GIFEncoder();
			try {
				for (int i = 0;; i++) {
					BufferedImage image = reader.read(i);
					for (TextOption option : options) {
						if (i >= option.getBegin() && i <= option.getEnd()) {
							image = ImgUtil.addText(image, option.getContent(), option.getOffsetX(),
									option.getOffsetY(), option.getFont(), option.getColor(), option.getColorShadow());
						}
					}
					if (i == 0) {
						encoder.init(image);
						encoder.setFrameRate(frameRate);
						encoder.start(dest);
					} else {
						encoder.addFrame(image);
					}
				}
			} catch (IndexOutOfBoundsException e) {
			}
			ciis.close();
			encoder.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据第index帧的delayTime值计算恒定帧率
	 * 
	 * @param gif
	 * @param index
	 * @return 恒定帧率
	 */
	public static int getAssumingFrameRate(File gif, int index) {
		try {
			ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();
			ImageInputStream ciis = ImageIO.createImageInputStream(gif);
			reader.setInput(ciis);
			IIOMetadata imageMetaData = reader.getImageMetadata(index);
			String metaFormatName = imageMetaData.getNativeMetadataFormatName();
			IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
			ciis.close();

			IIOMetadataNode graphicsControlExtensionNode = (IIOMetadataNode) root
					.getElementsByTagName("GraphicControlExtension").item(0);
			String delayTime = graphicsControlExtensionNode.getAttribute("delayTime");
			int frameRate = Integer.parseInt(delayTime);
			frameRate = frameRate == 0 ? 10 : 100 / frameRate;
			// System.out.println("frameRate: " + frameRate);
			return frameRate;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 根据第index帧的delayTime值计算恒定帧率
	 * 
	 * @param gif
	 * @param index
	 * @return 恒定帧率
	 */
	public static int getAssumingFrameRate(InputStream gif, int index) {
		try {
			ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();
			ImageInputStream ciis = ImageIO.createImageInputStream(gif);
			reader.setInput(ciis);
			IIOMetadata imageMetaData = reader.getImageMetadata(index);
			String metaFormatName = imageMetaData.getNativeMetadataFormatName();
			IIOMetadataNode root = (IIOMetadataNode) imageMetaData.getAsTree(metaFormatName);
			ciis.close();
			
			IIOMetadataNode graphicsControlExtensionNode = (IIOMetadataNode) root
					.getElementsByTagName("GraphicControlExtension").item(0);
			String delayTime = graphicsControlExtensionNode.getAttribute("delayTime");
			int frameRate = Integer.parseInt(delayTime);
			frameRate = frameRate == 0 ? 10 : 100 / frameRate;
			// System.out.println("frameRate: " + frameRate);
			return frameRate;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
