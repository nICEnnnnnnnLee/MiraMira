package top.nicelee.mirai.miramira.command;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.permission.Permission;
import top.nicelee.mirai.miramira.Main;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.RobotConstant;
import top.nicelee.mirai.miramira.util.HttpRequestUtil;
import top.nicelee.mirai.miramira.util.PermissionUil;

/**
 *  /gif [on|off]					开关动图加台词功能
 *	/gif help						查询帮助
 *	/gif list						查询当前存在的动图
 *	/gif [fetch|install|download]	下载动图
 */
@ACommand
public final class GifCommand extends JCompositeCommand {

	public GifCommand() {
		super(Main.INSTANCE, "gif");
		// 可选设置如下属性
		setDescription("MiraMira设置相关");
		setPermission(Permission.getRootPermission());
		setPrefixOptional(true);
	}

	@SubCommand("on")
	@Description("打开动图加台词功能")
	public void on(CommandSender sender) {
		RobotConfig.enableTxt2GifFunc = true;
		sender.sendMessage("动图加台词功能已打开");
	}

	@SubCommand("off")
	@Description("关闭动图加台词功能")
	public void off(CommandSender sender) {
		RobotConfig.enableTxt2GifFunc = false;
		sender.sendMessage("动图加台词功能已关闭");
	}

	@SubCommand("help")
	@Description("关闭动图加台词功能")
	public void help(CommandSender sender) {
		sender.sendMessage(
				"/gif [on|off]  开关动图加台词功能\r\n" 
				+ "/gif help  查询帮助\r\n"
				+ "/gif list  查询当前存在的动图\r\n" 
				+ "/gif [fetch|install|download]  下载动图\r\n"
				+ "举例，如果/gif list查询到了 真香.gif,那么可以尝试发送\r\n" 
				+ "/gif 真香 台词1 台词2 台词3 台词4");
	}

	@SubCommand("list")
	@Description("查询当前存在的动图")
	public void list(CommandSender sender) {
		File gifFolder = new File(RobotConfig.configFolder, "gifs");
		if (gifFolder.exists()) {
			StringBuilder sb = new StringBuilder();
			sb.append(" 当前存在的动图为：\n");
			File[] gifs = gifFolder.listFiles(f -> f.getName().endsWith(".gif"));
			for (File gif : gifs) {
				sb.append(" - ").append(gif.getName()).append("\n");
			}
			sender.sendMessage(sb.toString());
		} else
			sender.sendMessage("不存在已安装的动图");
	}

	@SubCommand({ "fetch", "install", "download" })
	@Description("下载动图")
	public void download(CommandSender sender) {
		sender.sendMessage("gif 正在进行安装，在没有结束之前请勿重复调用该命令");
		File gifFolder = new File(RobotConfig.configFolder, "gifs");
		if (!gifFolder.exists()) {
			gifFolder.mkdirs();
		}
//		https://butterandbutterfly.github.io/Q-Gif/pics/%E9%87%91%E9%A6%86%E9%95%BF%E6%96%97%E5%9B%BE/notext.gif

		int success = 0, fail = 0;
		StringBuilder tipsSucc = new StringBuilder(), tipsFail = new StringBuilder();
		for (String gifType : RobotConstant.GIF_LIST) {
			File gifFile = new File(gifFolder, gifType + ".gif");
			if (!gifFile.exists()) {
				String url = null;
				try {
					url = "https://butterandbutterfly.github.io/Q-Gif/pics/" + URLEncoder.encode(gifType, "UTF-8")
							+ "/notext.gif";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				File tempFile = new File(gifFolder, gifType + ".gif.tmp");
				HashMap<String, String> headers = new HashMap<>();
				headers.put("Host", "butterandbutterfly.github.io");
				headers.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
				headers.put("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:93.0) Gecko/20100101 Firefox/93.0");
				headers.put("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
				if (new HttpRequestUtil().download(url, tempFile, new HashMap<>())) {
					if (tempFile.renameTo(gifFile)) {
						tipsSucc.append(" - ").append(gifFile.getName()).append("\n");
						success++;
						continue;
					}
				}
				tipsFail.append(" - ").append(gifFile.getName()).append("\n");
				fail++;
			}
		}
		sender.sendMessage(String.format(
				"gif 安装完毕, %d 个成功, %d 个失败\n安装成功：\n%s安装失败：\n%s\n请自行下载 https://butterandbutterfly.github.io/Q-Gif/pics/[失败gif名]/notext.gif",
				success, fail, tipsSucc, tipsFail));
	}
}