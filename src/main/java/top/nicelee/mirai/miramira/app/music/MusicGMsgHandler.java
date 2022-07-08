package top.nicelee.mirai.miramira.app.music;

import java.util.List;
import java.util.function.Consumer;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MusicShare;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.RobotConstant;
import top.nicelee.mirai.miramira.app.music.libs.Music;
import top.nicelee.mirai.miramira.app.music.libs.NetEaseMusic;
import top.nicelee.mirai.miramira.handler.groupmsg.AGroupMsgHandler;

/**
 * 网易云 歌曲
 *
 */
@AGroupMsgHandler
public class MusicGMsgHandler implements Consumer<GroupMessageEvent> {

	@Override
	public void accept(GroupMessageEvent f) {
		if (!RobotConfig.enableMusicFunc) {
			return;
		}
		String gid = RobotConstant.PREFIX_QQ_GROUP_ID + f.getGroup().getId(); // 群号
		String fid = RobotConstant.PREFIX_QQ_FRIEND_ID + f.getSender().getId(); // QQ号
		if (RobotConfig.checkWhite && !RobotConfig.whitelist.contains(gid)) {
			// 群号不在白名单内则返回
			return;
		}
		if (RobotConfig.checkBlack && (RobotConfig.blacklist.contains(gid) || RobotConfig.blacklist.contains(fid))) {
			// 群号 或 QQ号在黑名单内则返回
			return;
		}
		String msg = f.getMessage().contentToString();
		if (msg.startsWith("网易云")) {
			// 判断查询的间隔时间是不是太短
			long currentTime = System.currentTimeMillis();
			long lastQueryTime = RobotConfig.musicRecords.getOrDefault(gid, 0L);
			if (currentTime - lastQueryTime < RobotConstant.MUSIC_QUERY_MIN_PERIOD) {
				return;
			}
			// 查询歌曲信息
			List<Music> musics = NetEaseMusic.Instance().searchWithoutLink(msg.substring(3).trim(), 10, 1);
			StringBuilder sb = new StringBuilder("已为你找到以下歌曲：\n");
			for (int i = 0; i < musics.size(); i++) {
				Music music = musics.get(i);
				sb.append(i).append("、").append(music.getName()).append(" - ").append(music.getSinger()).append("\n");
			}
			sb.append("发送序号即可点歌");
			RobotConfig.musicInfos.put(gid, musics);
			RobotConfig.musicRecords.put(gid, currentTime);
			f.getGroup().sendMessage(sb.toString());
			return;
		}

		int number = msg.charAt(0) - '0';
		if (msg.length() == 1 && number >= 0 && number <= 9) {
			List<Music> musics = RobotConfig.musicInfos.get(gid);
			if (musics != null && number < musics.size()) {
				Music music = musics.get(number);
				String url = NetEaseMusic.Instance().source(music.getId());
				MusicShare ms = music.to163MusicShare(url);
				f.getGroup().sendMessage(ms);
				return;
			}
		}
	}

}
