package top.nicelee.mirai.miramira.handler.groupmsg;

import java.util.function.Consumer;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.nicelee.mirai.miramira.RobotConfig;

/**
 * 测试群消息Handler
 *
 */
//@AGroupMsgHandler(priority = 2)
public class _TestGmsgHandler2 implements Consumer<GroupMessageEvent>{

	@Override
	public void accept(GroupMessageEvent t) {
		RobotConfig.logger.info("GroupMessageEvent priority2:" + t.getMessage().contentToString());
	}

}
