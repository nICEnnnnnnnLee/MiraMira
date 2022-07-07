package top.nicelee.mirai.miramira.util;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.ConsoleCommandSender;
import net.mamoe.mirai.console.command.GroupAwareCommandSender;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.RobotConstant;

public class PermissionUil {

	public static boolean isAdmin(CommandSender sender) {
//		RobotConfig.logger.debug("是否从控制台发出： " + (sender instanceof ConsoleCommandSender));
		if(sender instanceof ConsoleCommandSender)
			return true;
		if (RobotConfig.superadmins.contains(RobotConstant.PREFIX_QQ_ID + sender.getUser().getId())) 
			return true;
		return false;
	}
	
	/**
	 * 回复消息，提示没有权限
	 */
	public static void replyWithNoAuth(CommandSender sender) {
		MessageChainBuilder msg = new MessageChainBuilder().append("你没有权限进行此操作");
		if (sender instanceof GroupAwareCommandSender)
			msg.append(new At(sender.getUser().getId()));
		sender.sendMessage(msg.build());
	}
}
