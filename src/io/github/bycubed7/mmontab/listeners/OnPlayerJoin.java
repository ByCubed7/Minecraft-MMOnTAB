package io.github.bycubed7.mmontab.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.bycubed7.mmontab.MMOnTAB;
import io.github.bycubed7.mmontab.managers.badge.BadgeManager;

public class OnPlayerJoin implements Listener {

	MMOnTAB plugin;

	public OnPlayerJoin(MMOnTAB mmOnTAB) {
		plugin = mmOnTAB;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		BadgeManager.update(player);
	}
}
