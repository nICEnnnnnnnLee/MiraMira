package top.nicelee.mirai.miramira.util;

import net.mamoe.mirai.console.command.CommandSender;

/**
 * 工具类，用于确保有权限才会运行
 * new EnsureAuthUtil(sender, ()->{
 * 		System.out.println();
 * }).run();
 *
 */
public class EnsureAuthUtil {

	CommandSender sender;
	IRun runWithAuth;

	public EnsureAuthUtil(CommandSender sender, IRun runWithAuth) {
		this.sender = sender;
		this.runWithAuth = runWithAuth;
	}

	public boolean isValid() {
		boolean isValid = PermissionUil.isAdmin(sender);
		if (!isValid) {
			PermissionUil.replyWithNoAuth(sender);
		}
		return isValid;
	}

	public void run() {
		if (isValid() && runWithAuth != null) {
			runWithAuth.run();
		}
	}
	
	public interface IRun {
		void run();
	}
}
