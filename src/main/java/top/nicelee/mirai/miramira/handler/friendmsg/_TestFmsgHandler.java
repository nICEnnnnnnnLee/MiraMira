package top.nicelee.mirai.miramira.handler.friendmsg;

import java.util.function.Function;

import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import top.nicelee.mirai.miramira.RobotConfig;

/**
 * 测试好友消息Handler
 *Function<E, ListeningStatus>
 */
//@AFriendMsgHandler(priority = 6)
public class _TestFmsgHandler implements Function<FriendMessageEvent, ListeningStatus>{

	@Override
	public ListeningStatus apply(FriendMessageEvent t) {
		RobotConfig.logger.info("FriendMessageEvent priority6:" + t.getMessage().contentToString());
		return ListeningStatus.LISTENING;
	}


}
