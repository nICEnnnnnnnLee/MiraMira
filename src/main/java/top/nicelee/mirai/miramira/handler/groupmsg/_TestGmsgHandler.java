package top.nicelee.mirai.miramira.handler.groupmsg;

import java.util.function.Function;

import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import top.nicelee.mirai.miramira.RobotConfig;

/**
 * 测试群消息Handler
 *
 */
//@AGroupMsgHandler(priority = 66)
public class _TestGmsgHandler implements Function<GroupMessageEvent, ListeningStatus>{

	@Override
	public ListeningStatus apply(GroupMessageEvent t) {
		RobotConfig.logger.info("GroupMessageEvent priority66:" + t.getMessage().contentToString());
		return ListeningStatus.LISTENING;
	}


}
