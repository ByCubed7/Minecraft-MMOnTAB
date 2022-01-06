package io.github.bycubed7.mmontab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
//import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bycubed7.mmontab.commands.Reincarnate;
import io.github.bycubed7.mmontab.listeners.OnLevelup;
import io.github.bycubed7.mmontab.listeners.OnPlayerJoin;
import io.github.bycubed7.mmontab.managers.ConfigManager;
import io.github.bycubed7.mmontab.managers.DataManager;
import io.github.bycubed7.mmontab.managers.badge.BadgeManager;
import net.luckperms.api.LuckPerms;

public class MMOnTAB extends JavaPlugin {

	public static MMOnTAB instance;

	// Soft Dependencies
	public LuckPerms luckPerms;
	public boolean isLuckyPermsEnabled;

	@Override
	public void onEnable() {
		instance = this;
		new Debug(this);

		// Read config
		Debug.Log("Reading Config..");
		new ConfigManager(this, "default.yml");

		// Load save data
		Debug.Log("Loading DataManager..");
		new DataManager().load();

		Debug.Log("Loading BadgeManager..");
		new BadgeManager(this);

		// Get intergration api / settings
//		isLuckyPermsEnabled = GetLuckPerms();

		// Set up listeners
		Debug.Log("Setting up Event Listeners..");
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnLevelup(this), this);
		Bukkit.getServer().getPluginManager().registerEvents((Listener) new OnPlayerJoin(this), this);

		// Set up commands
		Debug.Log("Setting up Commands..");
		new Reincarnate(this);

		// Loading is done!
		Debug.Log(ChatColor.GREEN + "Done!");

		Debug.Banner();

		// Update all the players present
		// When /reload is called for example
		for (Player player : Bukkit.getOnlinePlayers())
			BadgeManager.update(player);

	}

//	public static boolean GetLuckPerms() {
//		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
//		if (provider != null) {
//			instance.luckPerms = provider.getProvider();
//			return true;
//		}
//		return false;
//	}
}
