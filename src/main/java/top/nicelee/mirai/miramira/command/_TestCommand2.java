package top.nicelee.mirai.miramira.command;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.permission.Permission;
import top.nicelee.mirai.miramira.Main;

//@ACommand
public final class _TestCommand2 extends JCompositeCommand  {

	public _TestCommand2() {
		super(Main.INSTANCE, "test");
		// 可选设置如下属性
		setDescription("MiraMira设置相关");
		setPermission(Permission.getRootPermission());
		setPrefixOptional(true);
	}

	// ...
    @SubCommand("name")
    public void foo(CommandSender sender, String msg) {
    	sender.sendMessage("收到msg:" +msg);
    }
    // ...
    public void name2(CommandContext context, String msg) {
    	context.getSender().sendMessage("收到msg2:" +msg);
    }
    
}