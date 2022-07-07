package top.nicelee.mirai.miramira.command;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.ConsoleCommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.permission.Permission;
import top.nicelee.mirai.miramira.Main;


//@ACommand
public final class _TestCommand extends JSimpleCommand {

	public _TestCommand() {
		super(Main.INSTANCE, "test");
		// 可选设置如下属性
		setDescription("这是一个测试指令");
//		setUsage("/test <target> <message>"); // 如不设置则自动根据带有 @Handler 的方法生成
		setPermission(Permission.getRootPermission());
		setPrefixOptional(true);
	}

	@Handler
	public void onCommand(CommandSender sender, String target, String msg) {
		Bot bot = Bot.getInstances().get(0); // 测试时获取第一个bot，正式应该从配置读取qq号，然后Bot.getInstance(long qq号)
		try {
			if(target.startsWith("f")) {
				bot.getFriend(Long.parseLong(target.substring(1))).sendMessage(msg);
			}else if(target.startsWith("g")) {
				bot.getGroup(Long.parseLong(target.substring(1))).sendMessage(msg);
			}else if(target.startsWith("m") && target.contains(".")) {
				String[] params = target.substring(1).split("\\.", 2);
				long groupNumber = Long.parseLong(params[0]);
				long memberNumber = Long.parseLong(params[1]);
				bot.getGroup(groupNumber).get(memberNumber).sendMessage(msg);
			}else {
				System.err.println("无法推断目标好友或群员\r\n- 好友: f+QQ号\r\n- 群: g+群号\r\n- 群成员: m+群号.QQ号");
				if(!(sender instanceof ConsoleCommandSender)) {
					throw new RuntimeException("无法推断目标好友或群员\r\n- 好友: f+QQ号\r\n- 群: g+群号\r\n- 群成员: m+群号.QQ号");
				}
			}
		}catch (Exception e) {
			System.err.println("无法推断目标好友或群员\r\n- 好友: f+QQ号\r\n- 群: g+群号\r\n- 群成员: m+群号.QQ号");
			if(!(sender instanceof ConsoleCommandSender)) {
				throw e;
			}
		}
	}
	
//	@Handler
//	public void onCommand(CommandSender sender, User target, String message) {
//		System.out.println(target);
//		System.out.println(message);
//		target.sendMessage(message);
//	}
//	
//	@Handler
//	public void onCommand(CommandSender sender, Group target, String msg1, String msg2) {
//		System.out.println(target);
//		System.out.println(msg1);
//		target.sendMessage(msg1);
//	}
//	
//	@Handler
//	public void test(CommandSender sender, String message) {
//		System.out.println(message);
//	}
}