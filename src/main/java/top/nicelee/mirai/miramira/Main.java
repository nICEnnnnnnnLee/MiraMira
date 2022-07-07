package top.nicelee.mirai.miramira;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.reflections.Reflections;

import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import top.nicelee.mirai.miramira.command.ACommand;
import top.nicelee.mirai.miramira.handler.friendmsg.AFriendMsgHandler;
import top.nicelee.mirai.miramira.handler.groupmsg.AGroupMsgHandler;
import top.nicelee.mirai.miramira.handler.normalmsg.ANormalMsgHandler;
import top.nicelee.mirai.plugin.PluginData;

public class Main extends JavaPlugin {
	public static final Main INSTANCE = new Main();

	private Main() {
		super(new JvmPluginDescriptionBuilder(PluginData.id, PluginData.version).name(PluginData.name)
				.author(PluginData.author).info(PluginData.info).build());
	}

	@Override
	public void onEnable() {
		// 初始化Robot配置
		initRobotConfig();

		Reflections reflections = new Reflections(PluginData.scanPackage);
		// 注册指令
		Set<Class<?>> cmdCls = reflections.getTypesAnnotatedWith(ACommand.class);
		for (Class<?> cls : cmdCls) {
			try {
				Command cmd = (Command) cls.newInstance();
				boolean r = CommandManager.INSTANCE.registerCommand(cmd, false); // 第二个参数false表示不override
				getLogger().info(String.format("命令行加载成功：%s\t%s", r, cls.getSimpleName()));
			} catch (Exception e) {
			}
		}
		// 注册群消息监听事件
		EventChannel<Event> eventChannel = GlobalEventChannel.INSTANCE.parentScope(this);
		Register(reflections, AGroupMsgHandler.class, eventChannel, GroupMessageEvent.class);
		Register(reflections, AFriendMsgHandler.class, eventChannel, FriendMessageEvent.class);
		Register(reflections, ANormalMsgHandler.class, eventChannel, MessageEvent.class);

		getLogger().info("插件加载完毕!");
	}

	/**
	 * 初始化Robot配置
	 */
	private void initRobotConfig() {
		RobotConfig.logger = getLogger();
		RobotConfig.configFolder = getConfigFolder();
		RobotConfig.dataFolder = getDataFolder();
		File configFile = new File(RobotConfig.configFolder, "config.yaml");
		// 先从自带的资源初始化设置
		StringBuilder defaultConfigYamlStr = new StringBuilder();
		try (BufferedReader r = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/defaultConfig.yaml")));) {
			String line = null;
			while ((line = r.readLine()) != null) {
				defaultConfigYamlStr.append(line).append("\r\n");
			}
			RobotConfig.fromYamlString(defaultConfigYamlStr.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 若不存在yaml，则创建默认配置
		if (!configFile.exists()) {
			RobotConfig.configFolder.mkdirs();
			try (FileOutputStream out = new FileOutputStream(configFile);) {
				out.write(defaultConfigYamlStr.toString().getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 从配置文件读取配置
		try (BufferedReader r = new BufferedReader(new FileReader(configFile));) {
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = r.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
			RobotConfig.fromYamlString(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 扫描reflections下包含注解handlerAnnotation的类，并在eventChannel注册，专门处理msgEventCls类型
	 * @param reflections
	 * @param handlerAnnotationCls
	 * @param eventChannel
	 * @param msgEventCls
	 */
	private void Register(Reflections reflections, Class<? extends Annotation> handlerAnnotationCls,
			EventChannel<Event> eventChannel, Class<? extends MessageEvent> msgEventCls) {
		Set<Class<?>> handlerClsSet = reflections.getTypesAnnotatedWith(handlerAnnotationCls);
		Class<?>[] handlerClsMatrix = handlerClsSet.toArray(new Class<?>[handlerClsSet.size()]);
		Arrays.sort(handlerClsMatrix, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				try {
					Annotation anno1 = o1.getAnnotation(handlerAnnotationCls);
					int pri1 = (int) anno1.getClass().getDeclaredMethod("priority").invoke(anno1);
					Annotation anno2 = o2.getAnnotation(handlerAnnotationCls);
					int pri2 = (int) anno2.getClass().getDeclaredMethod("priority").invoke(anno2);
					return pri2 - pri1;
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
		});
		for (Class<?> cls : handlerClsMatrix) {
			try {
				Object obj = cls.getConstructor().newInstance();
				if (obj instanceof Function) {
					eventChannel.subscribe(msgEventCls, (Function<MessageEvent, ListeningStatus>) obj);
				} else if (obj instanceof Consumer) {
					eventChannel.subscribeAlways(msgEventCls, (Consumer) obj);
				}

				Annotation anno1 = cls.getAnnotation(handlerAnnotationCls);
				int pri1 = (int) anno1.getClass().getDeclaredMethod("priority").invoke(anno1);
				getLogger().info(cls.getSimpleName() + " 插件优先级：" + pri1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
