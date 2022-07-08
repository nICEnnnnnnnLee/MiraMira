package top.nicelee.mirai.miramira.app.flash.pic.reply;

import java.util.function.Consumer;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.handler.groupmsg.AGroupMsgHandler;

/**
 * 闪照 回复 正常照片
 *
 */
@AGroupMsgHandler
public class FlashGMsgHandler implements Consumer<GroupMessageEvent> {

	@Override
	public void accept(GroupMessageEvent f) {
		if (!RobotConfig.enableFlash2NormalPicFunc) {
			return;
		}
		for (SingleMessage sm : f.getMessage()) {
			if(sm instanceof FlashImage) {
				FlashImage flash = (FlashImage)sm;
				f.getGroup().sendMessage(new MessageChainBuilder()
						.append(f.getSender().getNameCard())
						.append(" 的闪照:\n")
						.append(flash.getImage())
						.build()
				);
			}
		}
	}

}
