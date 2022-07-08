package top.nicelee.mirai.miramira.app.txt2gif;

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
import top.nicelee.mirai.miramira.command.ACommand;
import top.nicelee.mirai.miramira.util.HttpRequestUtil;

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
		sender.sendMessage("/gif [on|off]  开关动图加台词功能\r\n" + "/gif help  查询帮助\r\n" + "/gif list  查询当前存在的动图\r\n"
				+ "/gif [fetch|install|download]  下载动图\r\n" + "举例，如果/gif list查询到了 真香.gif,那么可以尝试发送\r\n"
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
		String tips = String.format("gif 安装完毕, %d 个成功, %d 个失败\n安装成功：\n%s安装失败：\n%s\n", success, fail, tipsSucc,
				tipsFail);
		if (fail > 0)
			tips += "请自行下载 https://butterandbutterfly.github.io/Q-Gif/pics/[失败gif名]/notext.gif";
		sender.sendMessage(tips);
	}
}