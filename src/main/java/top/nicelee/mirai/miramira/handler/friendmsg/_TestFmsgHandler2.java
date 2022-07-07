package top.nicelee.mirai.miramira.handler.friendmsg;

import java.util.function.Consumer;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import top.nicelee.mirai.miramira.RobotConfig;

/**
 * 测试好友消息Handler4
 *
 */
//@AFriendMsgHandler(priority = 2)
public class _TestFmsgHandler2 implements Consumer<FriendMessageEvent>{

	@Override
	public void accept(FriendMessageEvent t) {
		RobotConfig.logger.info("FriendMessageEvent2 priority2:" + t.getMessage().contentToString());
	}

}
