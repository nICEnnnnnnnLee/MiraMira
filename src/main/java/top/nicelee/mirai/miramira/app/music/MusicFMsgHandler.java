package top.nicelee.mirai.miramira.app.music;

import java.util.List;
import java.util.function.Consumer;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.message.data.MusicShare;
import top.nicelee.mirai.miramira.RobotConfig;
import top.nicelee.mirai.miramira.RobotConstant;
import top.nicelee.mirai.miramira.app.music.libs.Music;
import top.nicelee.mirai.miramira.app.music.libs.NetEaseMusic;
import top.nicelee.mirai.miramira.handler.friendmsg.AFriendMsgHandler;

/**
 * 网易云 歌曲
 *
 */
@AFriendMsgHandler
public class MusicFMsgHandler implements Consumer<FriendMessageEvent> {

	@Override
	public void accept(FriendMessageEvent f) {
		if (!RobotConfig.enableMusicFunc) {
			return;
		}
		String fid = RobotConstant.PREFIX_QQ_FRIEND_ID + f.getFriend().getId();
		if (RobotConfig.checkWhite && !RobotConfig.whitelist.contains(fid)) {
			// 不在白名单内则返回
			return;
		}
		if (RobotConfig.checkBlack && RobotConfig.blacklist.contains(fid)) {
			// 在黑名单内则返回
			return;
		}
		String msg = f.getMessage().contentToString();
		if (msg.startsWith("网易云")) {
			// 判断查询的间隔时间是不是太短
			long currentTime = System.currentTimeMillis();
			long lastQueryTime = RobotConfig.musicRecords.getOrDefault(fid, 0L);
			if (currentTime - lastQueryTime < RobotConstant.MUSIC_QUERY_MIN_PERIOD) {
				return;
			}
			// 查询歌曲信息
			List<Music> musics = NetEaseMusic.Instance().searchSimilar(msg.substring(3).trim(), 10, 1);
			StringBuilder sb = new StringBuilder("已为你找到以下歌曲：\n");
			for (int i = 0; i < musics.size(); i++) {
				Music music = musics.get(i);
				sb.append(i).append("、").append(music.getName()).append(" - ").append(music.getSinger()).append("\n");
			}
			sb.append("发送序号即可点歌");
			RobotConfig.musicInfos.put(fid, musics);
			RobotConfig.musicRecords.put(fid, currentTime);
			f.getSender().sendMessage(sb.toString());
			return;
		}

		int number = msg.charAt(0) - '0';
		if (msg.length() == 1 && number >= 0 && number <= 9) {
			List<Music> musics = RobotConfig.musicInfos.get(fid);
			if (musics != null && number < musics.size()) {
				Music music = musics.get(number);
				String url = NetEaseMusic.Instance().source(music.getId());
				MusicShare ms = music.to163MusicShare(url);
				f.getSender().sendMessage(ms);
				return;
			}
		}
	}

}
