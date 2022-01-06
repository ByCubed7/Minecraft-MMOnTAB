package io.github.bycubed7.mmontab.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

	public static ConfigManager instance;

	public JavaPlugin plugin;

	// Config
	private String filename;

	private File customConfigFile;
	private FileConfiguration config;

	public ConfigManager(JavaPlugin plugin, String filename) {
		instance = this;

		this.plugin = plugin;
		this.filename = filename;

		CreateConfig();

	}

	private void CreateConfig() {
		customConfigFile = new File(plugin.getDataFolder(), filename);
		if (!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdirs();
			plugin.saveResource(filename, false);
		}

		config = new YamlConfiguration();
		try {
			config.load(customConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static FileConfiguration iconfig() {
		return instance.config();
	}

	public FileConfiguration config() {
		return config;
	}

	public static String igetString(String path) {
		return instance.getString(path);
	}

	public String getString(String path) {
		return config.getString(path);
	}

	public static Integer igetInt(String path) {
		return instance.getInt(path);
	}

	public Integer getInt(String path) {
		return config.getInt(path);
	}

	public static Boolean igetBool(String path) {
		return instance.getBool(path);
	}

	public Boolean getBool(String path) {
		return config.getBoolean(path);
	}

	public static Color igetColor(String path) {
		return instance.getColor(path);
	}

	public Color getColor(String path) {
		return config.getColor(path);
	}
}
