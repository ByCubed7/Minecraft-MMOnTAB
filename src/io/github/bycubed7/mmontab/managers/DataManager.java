package io.github.bycubed7.mmontab.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import io.github.bycubed7.mmontab.managers.badge.Badges;

public class DataManager {

	static transient String filePath = "plugins/MMOnTAB/badges.data";
	static transient DataManager instance;

	HashMap<UUID, Badges> badges;
	HashMap<String, String> config;

	// Badge Value

	public static void setBadgeLevel(UUID id, String badge, Integer value) {
		Badges badges = getBadges(id);
		badges.put(badge, value);
		setBadges(id, badges);
	}

	public static Integer getBadgeLevel(UUID id, String badge) {
		Badges badges = getBadges(id);
		return badges.getLevel(badge);
	}

	public static Integer incrementBadgeLevel(UUID id, String badge) {
		Integer level = getBadgeLevel(id, badge);
		level++;
		setBadgeLevel(id, badge, level);
		return level;
	}

	public static void setBadges(UUID id, Badges badges) {
		instance.badges.put(id, badges);
		saveInstance(); // <-- May cause lag
	}

	public static Badges getBadges(UUID id) {
		Badges badges = instance.badges.get(id);
		if (badges == null)
			return new Badges();
		return badges;
	}

	// Config

	public static void setConfig(String key, String value) {
		instance.config.put(key, value);
		saveInstance(); // <-- May cause lag
	}

	public static String getConfig(String key) {
		String value = instance.config.get(key);
		return value;
	}

	// Con

	public DataManager() {
		if (instance == null)
			instance = this;

		badges = new HashMap<UUID, Badges>();
		config = new HashMap<String, String>();
	}

	public DataManager(HashMap<UUID, Badges> data) {
		instance.badges = data;
	}

	// Saving and Loading

	public static boolean saveInstance() {
		return instance.save();
	}

	public static SaveData loadInstance() {
		return instance.load();
	}

	public boolean save() {
		try {
			BukkitObjectOutputStream out = new BukkitObjectOutputStream(
					new GZIPOutputStream(new FileOutputStream(filePath)));

			SaveData newSaveFile = new SaveData();
			newSaveFile.badges = this.badges;
			newSaveFile.config = this.config;

			out.writeObject(newSaveFile);
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public SaveData load() {

		// If the file doesnt exist, create it!
		if (!new File(filePath).exists())
			save();

		try {
			BukkitObjectInputStream in = new BukkitObjectInputStream(
					new GZIPInputStream(new FileInputStream(filePath)));
			SaveData data = (SaveData) in.readObject();
			in.close();
			badges = data.badges;
			config = data.config;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			save();
			load(); // <-- Possible Bug: MAY CAUSE INFINITE LOOP
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
