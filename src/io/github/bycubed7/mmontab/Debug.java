package io.github.bycubed7.mmontab;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Debug {

	public static ConsoleCommandSender logger;

	public static String prefix;
	public static String version;
	public static String serverVersion;

	public Debug(MMOnTAB plugin) {
		prefix = "[" + plugin.getDescription().getPrefix() + "] ";
		version = plugin.getDescription().getVersion();
		logger = plugin.getServer().getConsoleSender();

		String a = plugin.getServer().getClass().getPackage().getName();
		serverVersion = a.substring(a.lastIndexOf('.') + 1);
	}

	public static void Log(String s) {
		logger.sendMessage(prefix + s);
	}

	public static void Log(String s, ChatColor color) {
		Log(color + s);
	}

	public static void Tell(Player player, String s) {
		// logger.info(prefix + " " + s);
		Log(player.getDisplayName() + " was told: " + s);
		player.sendMessage(s);
	}

	public static void Error(String s) {

	}

	public static void Banner() {

		// .sendMessage(ChatColor.DARK_GRAY + "Reload Complete");
		ChatColor c = ChatColor.GRAY;
		ChatColor a = ChatColor.DARK_GRAY;
		ChatColor v = ChatColor.DARK_GREEN;

		logger.sendMessage(c + "           __       ___       __  ");
		logger.sendMessage(c + "|\\/| |\\/| /  \\ |\\ |  |   /\\  |__)");
		logger.sendMessage(c + "|  | |  | \\__/ | \\|  |  /~~\\ |__)" + v + " v" + Debug.version);
		logger.sendMessage(v + "  Running on " + Debug.serverVersion + a + "   ~ By ByCubed7");
		logger.sendMessage("");
	}

}
