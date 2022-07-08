package top.nicelee.mirai.miramira.handler.normalmsg;

import java.io.File;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.function.Consumer;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.ExternalResource;
import top.nicelee.mirai.miramira.Main;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.RobotConstant;
import top.nicelee.mirai.miramira.app.gif.Gif;
import top.nicelee.mirai.miramira.util.PermissionUil;

/**
 * 文字转动图命令相关
 * e.g. /gif 真香 第一句台词 第二句台词 第三句台词 ...
 */
@ANormalMsgHandler
public class GifQMsgHandler implements Consumer<MessageEvent> {

	@Override
	public void accept(MessageEvent f) {
		String msg = f.getMessage().contentToString().toLowerCase();
		if (msg.startsWith("/gif ")) {
			// e.g. /gif 真香 第一句台词 第二句台词 第三句台词 ...
			String[] commands = msg.split(" ", 3);
			if (commands.length == 1)
				return;
			if (commands.length == 2) {
				CommandSender sender = CommandSender.from(f);
				if (!PermissionUil.isAdmin(CommandSender.from(f)))
					sender.sendMessage("你没有权限进行此操作");
				else
					CommandManager.INSTANCE.executeCommand(sender, f.getMessage(), false);
				return;
			}
			if (!RobotConfig.enableTxt2GifFunc) {
				return;
			}
			if (!isValidGifType(commands[1])) {
				f.getSubject().sendMessage(commands[1] + " 不是合法的动图类型");
				return;
			}

			File gif = new File(RobotConfig.configFolder, "gifs/" + commands[1] + ".gif");
			if (!gif.exists()) {
				String url = "https://butterandbutterfly.github.io/Q-Gif/pics/" + commands[1] + "/notext.gif";
				String path = gif.getParentFile().getAbsolutePath();
				f.getSubject().sendMessage(String.format("缺少动图源, 请下载文件%s ,并移至 %s\n", url, path));
				return;
			}
			String[] params = commands[2].trim().split(" ", 5);
			if (params[0].isEmpty())
				return;
			try {
				PipedInputStream pipedIn = new PipedInputStream();
				PipedOutputStream pipedOut = new PipedOutputStream(pipedIn);
				Main.INSTANCE.getScheduler().async(() -> {
//					Gif.gen("真香", pipedOut, "第一句台词", "第二句台词", "第三句台词", "第四句台词", "第五句台词");
					Gif.gen(commands[1], pipedOut, params);
				});
				ExternalResource.sendAsImage(pipedIn, f.getSubject());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	boolean isValidGifType(String cmd) {
		for (String cmdType : RobotConstant.GIF_LIST) {
			if (cmd.equals(cmdType))
				return true;
		}
		return false;
	}
}
