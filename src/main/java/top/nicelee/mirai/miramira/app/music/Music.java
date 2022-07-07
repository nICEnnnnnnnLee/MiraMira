package top.nicelee.mirai.miramira.app.music;

import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;

public class Music {

	String id;
	String name;
	String singer;
	String url;
	String picUrl;
	String album;
	String remark;

	/**
	 * 适用于信息完整的的Music对象
	 * @param url
	 * @return
	 */
	public MusicShare to163MusicShare() {
		MusicShare ms = new MusicShare(MusicKind.NeteaseCloudMusic, this.getName(),
				this.getSinger() + "-" + this.getAlbum(), "https://music.163.com/#/song?id=" + this.getId(), // 跳转路径
				this.getPicUrl(), // 封面路径
				this.url, // 音乐路径
				"[音乐]" + this.getName() + "-" + this.getSinger());
		return ms;
	}
	/**
	 * 适用于缺少歌曲直链的Music对象
	 * @param url	歌曲直链
	 * @return
	 */
	public MusicShare to163MusicShare(String url) {
		MusicShare ms = new MusicShare(MusicKind.NeteaseCloudMusic, this.getName(),
				this.getSinger() + "-" + this.getAlbum(), "https://music.163.com/#/song?id=" + this.getId(), // 跳转路径
				this.getPicUrl(), // 封面路径
				url, // 音乐路径
				"[音乐]" + this.getName() + "-" + this.getSinger());
		return ms;
	}

	public void print() {
		java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
		System.out.println(" --------");
		for(int i = 0 , len = fields.length; i < len; i++) {
			// 对于每个属性，获取属性名
			String varName = fields[i].getName();
			try {
				// 获取在对象f中属性fields[i]对应的对象中的变量
				String value = (String)fields[i].get(this);
				if( value != null && !value.equals("")){
					System.out.printf(" ---%s的值为: %s\n" ,varName, value);
				}
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println(" --------");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
