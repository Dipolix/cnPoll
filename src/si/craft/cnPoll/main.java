package si.craft.cnPoll;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import si.craft.cnPoll.components.Poll;
import si.craft.cnPoll.helpers.DefaultOptions;
import si.craft.cnPoll.helpers.Helper;
import si.craft.cnPoll.helpers.YAMLConfig;
import si.craft.cnPoll.helpers.YAMLConfigManager;

public class main extends JavaPlugin {
	// this plugin
		public static main plugin;
		public static String pluginPrefix = "CN Poll";
		
		// helper class
		public final Helper functions = new Helper();
		
		// config manager
		private YAMLConfigManager configManager;
		private YAMLConfig mainConfig;
		public DefaultOptions defaultoptions;
		
		// components
		public Poll poll;
		
		
		/************************************************************************************************
		 * ON PLUGIN :: ENABLE / RELOAD
		 ***********************************************************************************************/
		@Override
		public void onEnable() {
			
			// set plugin
			plugin = this;
			
			// get plugin.yml file contents
			PluginDescriptionFile description = this.getDescription();
			
			/************************************************************************************************
			 * PLUGIN CONFIG
			 ***********************************************************************************************/
			configManager = new YAMLConfigManager(this);
			String[] header = { pluginPrefix, "----- CN Poll -----"};
			
			// default config file (config.yml)
			defaultoptions = new DefaultOptions(this);
			try {
				mainConfig = configManager.getNewConfig("cnPoll.yml", header);
				defaultoptions.setDefaultValues(mainConfig);
			} 
			catch (Exception e) {}
			mainConfig.reloadConfig();
			
			/************************************************************************************************
			 * START COMPONENTS
			 ***********************************************************************************************/
			poll = new Poll();

			/************************************************************************************************
			 * LOG PLUGIN INIT
			 ***********************************************************************************************/
			Helper.log("&e Version: " + description.getVersion() + "&a Has Been Enabled!");
			
		}
		
		/************************************************************************************************
		 * COMMANDS
		 ***********************************************************************************************/
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			// lowercase incomming command
			String command = cmd.getName().toLowerCase();
			
			// test for command origin
			if (sender instanceof Player) {
				switch (command) {
				case "poll":
					Poll.doPoll(sender, cmd, command, args);
					break;
				}
				
			}
			return false;
		}
		
		/************************************************************************************************
		 * ON PLUGIN :: DISABLE
		 ***********************************************************************************************/
		@Override
		public void onDisable() {
			// console message on disable
			Helper.log("&aHas Been Disabled!");
		}
		
		/************************************************************************************************
		 * GET PLUGIN
		 ***********************************************************************************************/
		public static main getPlugin() {
			return plugin;
		}

		/************************************************************************************************
		 * PLUGIN CONFIG FILES
		 ***********************************************************************************************/
		public YAMLConfig getMainConfig() {
			return mainConfig;
		}
}