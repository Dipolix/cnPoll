package si.craft.cnPoll.helpers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import si.craft.cnPoll.main;

public class Helper {
	
	// plugin prefix
	public static String mainPrefix = main.pluginPrefix;
	
	/************************************************************************************************
	 * COLORIZE STRING
	 ***********************************************************************************************/
	public static String colors(String string) {
		if (string == null)
			return null;
		string = string.replaceAll("&([0-9a-fA-Fk-pK-PrR])", "\u00A7$1");
		string = string.replaceAll("\u00A7P", "\u00A7p");
		return string;
	}

	/************************************************************************************************
	 * MESSAGE CONSTRUCTOR
	 ***********************************************************************************************/
	public static String mss(String prefix, String message) {
		return colors("&8[&3" + mainPrefix + "&8]&b " + message);
	}
	
	/************************************************************************************************
	 * MESSAGE NO PERMISSION
	 ***********************************************************************************************/
	public static void noPermission(Player player) {
		String noPermission = (String) main.getPlugin().getMainConfig().get("messages.NoPermission");
		player.sendMessage(colors(noPermission));
	}
	
	/************************************************************************************************
	 * BROADCAST BACKUP / AUTOSAVE
	 ***********************************************************************************************/
	public static void BroadcastAutoSave(String type) {
		String startAutoSave = (String) main.getPlugin().getMainConfig().get("messages.AutoSave.Start");
		String endAutoSave = (String) main.getPlugin().getMainConfig().get("messages.AutoSave.End");
		if(type.equalsIgnoreCase("START")) {
			Bukkit.broadcastMessage(Helper.colors(startAutoSave));
		}
		else if(type.equalsIgnoreCase("END")) {
			Bukkit.broadcastMessage(Helper.colors(endAutoSave));
		}
	}
	
	/************************************************************************************************
	 * MESSAGE FROM COMMAND ARGUMENTS
	 ***********************************************************************************************/
	public static String argMessage(String[] message) {
		String mss = null;
		for (int i = 0; i < message.length; i++) {
			if (i != 0) {
				mss = mss + " " + message[i];
			} 
			else {
				mss = message[i];
			}
		}
		return mss;
	}

	/************************************************************************************************
	 * PERMISSION MESSAGES
	 ***********************************************************************************************/
	public static void permMss(String message, boolean access, Player player) {
		String noPermission = (String) main.getPlugin().getMainConfig().get("messages.NoPermission");
		if (!access) {
			player.sendMessage(Helper.colors(noPermission));
		}
	}

	/************************************************************************************************
	 * PREFIX CONSTRUCTOR
	 ***********************************************************************************************/
	public static String prefixConstructor(String prefix) {
		int boxed = 3;
		int prefixLength = prefix.length();
		String addSpaces = "";
		if (prefixLength < boxed) {
			int toShort = boxed - prefixLength;
			for (int i = 0; i < toShort; i++) {
				addSpaces = addSpaces + " ";
			}
		}
		prefix = prefix + addSpaces;

		return prefix;
	}

	/************************************************************************************************
	 * LOGGERS
	 ***********************************************************************************************/
	public static void log(String message) {
		Bukkit.getConsoleSender().sendMessage(colors("&8[&3" + mainPrefix + "&8] &f:&e " + message));
	}

	public static void warning(String message) {
		Bukkit.getConsoleSender().sendMessage(colors("&e[Warning] &c[&a" + mainPrefix + "&c] &f:&e " + message));
	}

	public static void severe(String message) {
		Bukkit.getConsoleSender().sendMessage(colors("&4[Severe] &c[&a" + mainPrefix + "&c] &f:&e " + message));
	}
}

