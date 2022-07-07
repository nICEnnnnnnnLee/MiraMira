package top.nicelee.mirai.miramira.handler.normalmsg;

import java.util.function.Consumer;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.event.events.MessageEvent;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.RobotConstant;

/**
 * 操作命令相关
 *
 */
@ANormalMsgHandler
public class CmdMsgHandler implements Consumer<MessageEvent> {

	@Override
	public void accept(MessageEvent f) {
		String msg = f.getMessage().contentToString();
		if (isCommand(msg)) {
			RobotConfig.logger.debug("收到了一条指令：" + msg);
			CommandManager.INSTANCE.executeCommand(CommandSender.from(f), f.getMessage(), false);
		}
	}

	boolean isCommand(String msg) {
		for (String cmd : RobotConstant.CMD_MSG_LIST) {
			if (msg.startsWith(cmd))
				return true;
		}
		return false;
	}

}
