package top.nicelee.mirai.miramira.app.txt2gif.libs.gif;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.app.txt2gif.libs.gif.GifUtil;
import top.nicelee.mirai.miramira.app.txt2gif.libs.gif.ImgUtil;
import top.nicelee.mirai.miramira.app.txt2gif.libs.gif.bean.TextOption;

public class Gif {

	private static HashMap<String, int[][]> gifConfigs;
	static {
		gifConfigs = new HashMap<>(20, 0.9f);
		// { {台词数量, 文本加词位置Y坐标}, { gif宽度, gif高度}, {字体类型, 字体颜色类型, 阴影颜色类型}, { 台词1起始帧,
		// 台词1结束帧 }, ...
		// }
		// gifConfigs.put("谁赞成谁反对", new int[][] { { num, y }, {0, 0}, { width, height },
		// });
		gifConfigs.put("真香",
				new int[][] { { 4, 165 }, { 298, 184 }, { 0, 0, 2 }, { 0, 8 }, { 12, 23 }, { 25, 34 }, { 37, 47 } });
		gifConfigs.put("谁赞成谁反对",
				new int[][] { { 3, 150 }, { 300, 168 }, { 0, 0, 2 }, { 4, 16 }, { 18, 35 }, { 37, 44 } });
		gifConfigs.put("元首骂人",
				new int[][] { { 3, 110 }, { 200, 114 }, { 0, 0, 2 }, { 0, 31 }, { 43, 67 }, { 68, 96 } });
		gifConfigs.put("我卢本伟没有开挂",
				new int[][] { { 3, 210 }, { 300, 225 }, { 1, 0, 1 }, { 0, 12 }, { 14, 20 }, { 21, 33 } });
		gifConfigs.put("充钱就能解决", new int[][] { { 2, 180 }, { 397, 196 }, { 1, 0, 1 }, { 1, 12 }, { 16, 35 } });
		gifConfigs.put("张学友万恶之源", new int[][] { { 1, 200 }, { 335, 218 }, { 1, 0, 2 }, { 0, 20 } });
		gifConfigs.put("金馆长斗图",
				new int[][] { { 3, 160 }, { 300, 170 }, { 2, 0, 2 }, { 1, 61 }, { 85, 107 }, { 137, 175 } });
		gifConfigs.put("你那是馋她身子",
				new int[][] { { 3, 169 }, { 280, 179 }, { 2, 0, 1 }, { 2, 10 }, { 18, 30 }, { 31, 44 } });
		gifConfigs.put("外卖小哥我信你个鬼", new int[][] { { 2, 230 }, { 250, 250 }, { 1, 0, 1 }, { 1, 16 }, { 18, 36 } });
		gifConfigs.put("张全蛋警告",
				new int[][] { { 3, 150 }, { 300, 167 }, { 2, 0, 1 }, { 1, 7 }, { 12, 19 }, { 23, 45 } });
		gifConfigs.put("你好骚哦", new int[][] { { 1, 197 }, { 240, 209 }, { 2, 0, 1 }, { 19, 30 } });
		gifConfigs.put("打人耳光", new int[][] { { 1, 188 }, { 300, 200 }, { 2, 0, 2 }, { 0, 35 } });
		gifConfigs.put("吔屎啦梁非凡",
				new int[][] { { 4, 155 }, { 300, 167 }, { 2, 0, 1 }, { 0, 9 }, { 10, 19 }, { 20, 36 }, { 37, 44 } });
		gifConfigs.put("为所欲为", new int[][] { { 9, 120 }, { 236, 132 }, { 0, 0, 1 }, { 8, 9 }, { 20, 26 }, { 32, 44 },
				{ 46, 59 }, { 61, 69 }, { 72, 78 }, { 83, 97 }, { 107, 117 }, { 118, 124 } });
	}

	public static void main(String[] a) throws FileNotFoundException {
//		for(String gifType: gifConfigs.keySet()) {
//			FileOutputStream output = new FileOutputStream(gifType + ".gif");
//			Bot.gen(gifType, output, "第一句台词", "第二句台词", "第三句台词", "第四句台词", "第五句台词");
//		}
//		String gifType = "吔屎啦梁非凡";
		String gifType = "真香";
		FileOutputStream output = new FileOutputStream(gifType + ".gif");
		Gif.gen(gifType, output, "第一句台词", "第二句台词", "第三句台词", "第四句台词", "第五句台词");
	}

	/**
	 * 给Gif添加台词
	 * 
	 * @param gifType
	 * @param fout    gif输出流
	 * @param strs    台词
	 */
	public static void gen(String gifType, OutputStream fout, String... strs) {
		int[][] gifConfig = gifConfigs.get(gifType);
		if (gifConfig == null) {
			System.err.println("不存在当前gifType：" + gifType);
			return;
		}
		gen(gifType, gifConfig, fout, strs);
	}

	/**
	 * 给Gif添加台词
	 * 
	 * @param gifType
	 * @param gifConfig 可以调用getGifConfig获得
	 * @param fout      gif输出流
	 * @param strs      台词
	 */
	public static void gen(String gifType, int[][] gifConfig, OutputStream fout, String... strs) {
		int num = gifConfig[0][0];
		if (strs.length < num) {
			String[] temp = new String[num];
			Arrays.fill(temp, " ");
			System.arraycopy(strs, 0, temp, 0, strs.length);
			strs = temp;
//			System.err.println("台词数量不够");
//			throw new RuntimeException("台词数量不够");
		}
		int offsetY = gifConfig[0][1];
		int width = gifConfig[1][0];
		Font font = TextOption.fonts[gifConfig[2][0]];
		Color color = TextOption.colors[gifConfig[2][1]];
		Color colorShadow = TextOption.colors[gifConfig[2][2]];
		List<TextOption> options = new ArrayList<TextOption>();
		for (int i = 0; i < gifConfig.length - 3; i++) {
			TextOption option = new TextOption(gifConfig[i + 3][0], gifConfig[i + 3][1], strs[i],
					ImgUtil.offsetXCenter(width, strs[i], font), offsetY, font, color, colorShadow);
			options.add(option);
		}
		run(gifType, fout, options);
	}

	private static void run(String gifType, OutputStream fout, List<TextOption> options) {
		try {
			File gif = new File(RobotConfig.configFolder, "gifs/" + gifType + ".gif");
			FileInputStream source = new FileInputStream(gif);
			int frameRate = GifUtil.getAssumingFrameRate(gif, 1);
//			InputStream source = Gif.class.getResourceAsStream("/pics/" + gifType + "/notext.gif");
//			int frameRate = GifUtil.getAssumingFrameRate(source, 1);
//			source.close();
//			source = Gif.class.getResourceAsStream("/pics/" + gifType + "/notext.gif");
			GifUtil.addText(source, fout, options, frameRate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[][] getGifConfig(String gifType) {
		return gifConfigs.get(gifType);
	}
}
