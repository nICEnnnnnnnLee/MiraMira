package top.nicelee.mirai.miramira;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import io.ktor.util.collections.ConcurrentSet;
import kotlinx.coroutines.debug.internal.ConcurrentWeakMap;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.yamlkt.Yaml;
import top.nicelee.mirai.miramira.app.music.Music;

public class RobotConfig {

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	private @interface Config {
		String description();
	}
	
	public static MiraiLogger logger;

	public static File configFolder;
	public static File dataFolder;

	@Config(description="是否开启点歌功能")
	public static boolean enableMusicFunc;
	@Config(description="是否开启闪照变正常功能")
	public static boolean enableFlash2NormalPicFunc=true;
	@Config(description="是否开启动图添加文字功能")
	public static boolean enableTxt2GifFunc=true;
	@Config(description="是否开启白名单校验, 开启后只有在白名单里才能校验通过")
	public static boolean checkWhite;
	@Config(description="是否开启黑名单校验, 开启后只有不在黑名单里才能校验通过")
	public static boolean checkBlack;
	@Config(description="管理员账号列表, e.g. [q10000, q10086]")
	public static Set<String> superadmins;
	@Config(description="白名单列表, e.g. [f10000, g10086]表示qq 10000, 和群 10086" )
	public static Set<String> whitelist;
	@Config(description="黑名单列表, e.g. [f10000, g10086]表示qq 10000, 和群 10086")
	public static Set<String> blacklist;
	public static Map<String, Long> musicRecords = new ConcurrentWeakMap<>();
	public static Map<String, List<Music>> musicInfos = new ConcurrentWeakMap<>();

	public static String status() {
		StringBuilder sb = new StringBuilder();
		sb.append("enableMusicFunc: ").append(enableMusicFunc).append("\n");
		sb.append("enableFlash2NormalPicFunc: ").append(enableMusicFunc).append("\n");
		sb.append("enableTxt2GifFunc: ").append(enableTxt2GifFunc).append("\n");
		sb.append("checkWhite: ").append(checkWhite).append("\n");
		sb.append("checkBlack: ").append(checkBlack).append("\n");
		sb.append("superadmins: ").append(superadmins).append("\n");
		sb.append("whitelist: ").append(whitelist).append("\n");
		sb.append("blacklist: ").append(blacklist).append("\n");
		return sb.toString();
	}

	public static String toYamlString() {
		Map<String, Object> map = new WeakHashMap<>();
		Field[] fields = RobotConfig.class.getDeclaredFields();
		for (Field field : fields) {
			if(field.getAnnotation(Config.class) == null) {
//				System.out.println(field.getName() + " - " +(field.getAnnotation(Config.class) == null));
				continue;
			}
			try {
				Object value = field.get(null);
				if (value != null) {
					if (field.getType().equals(Set.class)) {
						Set<String> set = (Set<String>)value;
						map.put(field.getName(), set.toArray(new String[set.size()]));
					} else {
						map.put(field.getName(), value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Yaml.Default.encodeToString(map);
	}

	public static void fromYamlString(String yamlStr) {
		Map<String, Object> map = Yaml.Default.decodeMapFromString(yamlStr);
		if (map == null)
			return;
		// 反射设置 RobotConfig
		Field[] fields = RobotConfig.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				Object value = map.get(field.getName());
				if (value != null) {
					if (field.getType().equals(Set.class)) {
						ConcurrentSet<String> set = new ConcurrentSet<String>();
						set.addAll((List<String>) value);
						field.set(null, set);
					} else {
						field.set(null, value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
