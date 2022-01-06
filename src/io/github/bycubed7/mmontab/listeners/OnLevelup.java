package io.github.bycubed7.mmontab.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.util.player.UserManager;

import io.github.bycubed7.mmontab.Debug;
import io.github.bycubed7.mmontab.MMOnTAB;
import io.github.bycubed7.mmontab.managers.DataManager;
import io.github.bycubed7.mmontab.managers.badge.BadgeManager;

public class OnLevelup implements Listener {

	MMOnTAB plugin;

	public OnLevelup(MMOnTAB mmOnTAB) {
		plugin = mmOnTAB;
	}

	@EventHandler
	public void onPlayerLevelUp(McMMOPlayerLevelUpEvent event) {
		Player player = event.getPlayer();
		McMMOPlayer mmoPlayer = UserManager.getPlayer(player);
		PrimarySkillType skill = event.getSkill();

		if (mmoPlayer.hasReachedLevelCap(skill)) {

			Debug.Log(String.format("%s has reached Level Cap", player.getDisplayName()));

			DataManager.incrementBadgeLevel(player.getUniqueId(), skill.name());
			BadgeManager.update(player);
		}
	}
}
