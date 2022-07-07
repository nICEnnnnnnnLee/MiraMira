package top.nicelee.mirai.miramira.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.permission.Permission;
import top.nicelee.mirai.miramira.Main;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.util.PermissionUil;

/**
 *  /music [on|off]	开关点歌功能
 */
@ACommand
public final class MusicCommand extends JSimpleCommand {

	public MusicCommand() {
		super(Main.INSTANCE, "music");
		// 可选设置如下属性
		setDescription("点歌命令相关");
		setPermission(Permission.getRootPermission());
		setPrefixOptional(true);
	}

	@Handler
	public void onCommand(CommandSender sender) {
		sender.sendMessage("/music [on|off]	开关点歌功能");
	}
	/**
	 *  /music [on|off]	开关点歌功能
	 */
	@Handler
	public void onCommand(CommandSender sender, String flag) {
		// 权限判断
		if(!PermissionUil.isAdmin(sender)){
			sender.sendMessage("你没有权限进行此操作");
			return;
		}
		if ("on".equalsIgnoreCase(flag)) {
			RobotConfig.enableMusicFunc = true;
			sender.sendMessage("点歌功能已开启");
		} else if ("off".equalsIgnoreCase(flag)) {
			RobotConfig.enableMusicFunc = false;
			sender.sendMessage("点歌功能已关闭");
		} else {
			sender.sendMessage("无效的flag：" + flag);
		}
	}

}