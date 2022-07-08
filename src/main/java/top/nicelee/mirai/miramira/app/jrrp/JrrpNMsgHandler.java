package top.nicelee.mirai.miramira.app.jrrp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;

import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.handler.normalmsg.ANormalMsgHandler;

/**
 * Jrrp命令相关
 *
 */
@ANormalMsgHandler
public class JrrpNMsgHandler implements Consumer<MessageEvent> {

	@Override
	public void accept(MessageEvent f) {
		if(!RobotConfig.enableJrrpFunc)
			return;
		String msg = f.getMessage().contentToString().toLowerCase();
		if (msg.equals(".jrrp") || msg.equals("。jrrp")) {
			User user = f.getSender();
			
			String ff = new SimpleDateFormat("yyyyMMdd").format(new Date()) + user.getId();
			int jrrp = new Random(toHash(ff)).nextInt(101);
			
			MessageChainBuilder replyMsg = new MessageChainBuilder();
			if (user instanceof Member)
				replyMsg.append(new At(user.getId()));
			replyMsg.add(" 你的jrrp为: " + jrrp);
			f.getSubject().sendMessage(replyMsg.build());
		}
	}

	public static int toHash(String key) {
		int arraySize = 11113; // 数组大小一般取质数
		int hashCode = 0;
		for (int i = 0; i < key.length(); i++) { // 从字符串的左边开始计算
			int letterValue = key.charAt(i) - 96;// 将获取到的字符串转换成数字，比如a的码值是97，则97-96=1
													// 就代表a的值，同理b=2；
			hashCode = ((hashCode << 5) + letterValue) % arraySize;// 防止编码溢出，对每步结果都进行取模运算
		}
		return hashCode;
	}

}
