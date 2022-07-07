package top.nicelee.mirai.miramira.command;

import java.io.File;
import java.io.FileOutputStream;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.permission.Permission;
import top.nicelee.mirai.miramira.Main;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.RobotConstant;
import top.nicelee.mirai.miramira.util.PermissionUil;

/**
 * /miramira help 查询帮助
 * 
 * /miramira status 查看状态
 * 
 * /miramira save 保存当前配置
 * 
 * /miramira flashPicReply [on|off] 开关闪照变正常照片功能
 * 
 * /miramira whitelist [on|off] 开关白名单检测
 * 
 * /miramira whitelist add [g群号|fQQ号] 添加白名单
 * /miramira whitelist delete [g群号|fQQ号] 从白名单去掉
 * 
 * /miramira blacklist [on|off] 开关黑名单检测
 * 
 * /miramira blacklist add [g群号|fQQ号] 添加黑名单
 * /miramira blacklist delete [g群号|fQQ号] 从黑名单去掉
 */
@ACommand
public final class MiraMiraCommand extends JCompositeCommand {

	public MiraMiraCommand() {
		super(Main.INSTANCE, "miramira");
		// 可选设置如下属性
		setDescription("MiraMira设置相关");
		setPermission(Permission.getRootPermission());
		setPrefixOptional(true);
	}

	@SubCommand({"help", "h"})
	@Description("查询帮助")
	public void help(CommandSender sender) {
		sender.sendMessage(
				"/miramira help|h 查询帮助\r\n" 
				+ "/miramira status|st 查看状态\r\n"
				+ "/miramira save|s 保存当前配置\r\n"
				+ "/miramira flashPicReply|fpr [on|off] 开关闪照变正常照片功能\r\n"
				+ "/miramira whitelist|w [on|off] 开关白名单检测\r\n"
				+ "/miramira whitelist|w add [g群号|fQQ号] 添加白名单\r\n"
				+ "/miramira whitelist|w delete [g群号|fQQ号] 从白名单去掉\r\n"
				+ "/miramira blacklist|b [on|off] 开关黑名单检测\r\n"
				+ "/miramira blacklist|b add [g群号|fQQ号] 添加黑名单\r\n" 
				+ "/miramira blacklist|b delete [g群号|fQQ号] 从黑名单去掉");
	}
	
	@SubCommand({"status", "st"})
	@Description("查看状态")
	public void status(CommandSender sender) {
		if (!PermissionUil.isAdmin(sender)) {
			PermissionUil.replyWithNoAuth(sender);
			return;
		}
		sender.sendMessage(RobotConfig.status());
	}

	@SubCommand({"save", "s"})
	@Description("保存当前配置")
	public void save(CommandSender sender) {
		if (!PermissionUil.isAdmin(sender)) {
			PermissionUil.replyWithNoAuth(sender);
			return;
		}
		File configFile = new File(RobotConfig.configFolder, "config.yaml");
		if (!configFile.exists())
			RobotConfig.configFolder.mkdirs();
		try (FileOutputStream out = new FileOutputStream(configFile);) {
			out.write(RobotConfig.toYamlString().getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		sender.sendMessage("已保存配置");
	}

	/**
	 * /miramira flashPicReply|fpr [on|off] 开关闪照变正常照片功能
	 * @param sender
	 * @param flag	on|off
	 */
	@SubCommand({"flashPicReply", "fpr"})
	@Description("开关闪照变正常照片功能")
	public void enableFlash2NormalPicFunc(CommandSender sender, String flag) {
		// 权限判断
		if (!PermissionUil.isAdmin(sender)) {
			PermissionUil.replyWithNoAuth(sender);
			return;
		}
		if ("on".equalsIgnoreCase(flag)) {
			RobotConfig.enableFlash2NormalPicFunc = true;
			sender.sendMessage("回复闪照检测已开启");
		} else if ("off".equalsIgnoreCase(flag)) {
			RobotConfig.enableFlash2NormalPicFunc = false;
			sender.sendMessage("回复闪照检测已关闭");
		} else {
			sender.sendMessage("/miramira flashPicReply|fpr " + flag + " 无法解析");
		}
	}
	
	/**
	 * /miramira whitelist [on|off] 开关白名单检测
	 * @param sender
	 * @param flag	on|off
	 */
	@SubCommand({"whitelist", "w"})
	@Description("开关白名单检测")
	public void dealWithWhiteList(CommandSender sender, String flag) {
		// 权限判断
		if (!PermissionUil.isAdmin(sender)) {
			PermissionUil.replyWithNoAuth(sender);
			return;
		}
		if ("on".equalsIgnoreCase(flag)) {
			RobotConfig.checkWhite = true;
			sender.sendMessage("白名单检测已开启");
		} else if ("off".equalsIgnoreCase(flag)) {
			RobotConfig.checkWhite = false;
			sender.sendMessage("白名单检测已关闭");
		} else {
			sender.sendMessage("/miramira whitelist " + flag + " 无法解析");
		}
	}

	/**
	 * /miramira whitelist add [g群号|fQQ号] 添加白名单
	 * /miramira whitelist delete [g群号|fQQ号] 从白名单去掉
	 * @param sender
	 * @param cmdType	add|delete
	 * @param cmdContent g群号|fQQ号
	 */
	@SubCommand({"whitelist", "w"})
	@Description("添加/删除白名单")
	public void dealWithWhiteList(CommandSender sender, String cmdType, String cmdContent) {
		// 权限判断
		if (!PermissionUil.isAdmin(sender)) {
			PermissionUil.replyWithNoAuth(sender);
			return;
		}
		switch (cmdType) {
		case "add":
			if (cmdContent.startsWith(RobotConstant.PREFIX_QQ_FRIEND_ID)
					|| cmdContent.startsWith(RobotConstant.PREFIX_QQ_GROUP_ID)) {
				RobotConfig.whitelist.add(cmdContent);
				sender.sendMessage(cmdContent + "已加入白名单");
			}
			return;
		case "delete":
			if (cmdContent.startsWith(RobotConstant.PREFIX_QQ_FRIEND_ID)
					|| cmdContent.startsWith(RobotConstant.PREFIX_QQ_GROUP_ID)) {
				RobotConfig.whitelist.remove(cmdContent);
				sender.sendMessage(cmdContent + "已从白名单删除");
			}
			return;
		}
		sender.sendMessage("/miramira whitelist" + cmdType + " " + cmdContent + " 无法解析");
	}

	/**
	 * /miramira blacklist [on|off] 开关黑名单检测
	 * @param sender
	 * @param flag	on|off
	 */
	@SubCommand({"blacklist", "b"})
	@Description("开关黑名单检测")
	public void dealWithBlackList(CommandSender sender, String flag) {
		// 权限判断
		if (!PermissionUil.isAdmin(sender)) {
			PermissionUil.replyWithNoAuth(sender);
			return;
		}
		if ("on".equalsIgnoreCase(flag)) {
			RobotConfig.checkBlack = true;
			sender.sendMessage("黑名单检测已开启");
		} else if ("off".equalsIgnoreCase(flag)) {
			RobotConfig.checkBlack = false;
			sender.sendMessage("黑名单检测已关闭");
		} else {
			sender.sendMessage("/miramira blacklist " + flag + " 无法解析");
		}
	}

	/**
	 * /miramira blacklist add [g群号|fQQ号] 添加白名单
	 * /miramira blacklist delete [g群号|fQQ号] 从白名单去掉
	 * @param sender
	 * @param cmdType	add|delete
	 * @param cmdContent g群号|fQQ号
	 */
	@SubCommand({"blacklist","b"})
	@Description("添加/删除白名单")
	public void dealWithBlackList(CommandSender sender, String cmdType, String cmdContent) {
		// 权限判断
		if (!PermissionUil.isAdmin(sender)) {
			PermissionUil.replyWithNoAuth(sender);
			return;
		}
		switch (cmdType) {
		case "add":
			if (cmdContent.startsWith(RobotConstant.PREFIX_QQ_FRIEND_ID)
					|| cmdContent.startsWith(RobotConstant.PREFIX_QQ_GROUP_ID)) {
				RobotConfig.blacklist.add(cmdContent);
				sender.sendMessage(cmdContent + "已加入黑名单");
			}
			return;
		case "delete":
			if (cmdContent.startsWith(RobotConstant.PREFIX_QQ_FRIEND_ID)
					|| cmdContent.startsWith(RobotConstant.PREFIX_QQ_GROUP_ID)) {
				RobotConfig.blacklist.remove(cmdContent);
				sender.sendMessage(cmdContent + "已从黑名单删除");
			}
			return;
		}
		sender.sendMessage("/miramira blacklist" + cmdType + " " + cmdContent + " 无法解析");
	}

}