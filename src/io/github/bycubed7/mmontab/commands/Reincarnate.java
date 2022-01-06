package io.github.bycubed7.mmontab.commands;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.SkillAPI;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import com.gmail.nossr50.util.player.UserManager;

import io.github.bycubed7.mmontab.Debug;
import io.github.bycubed7.mmontab.MMOnTAB;
import io.github.bycubed7.mmontab.managers.ConfigManager;
import io.github.bycubed7.mmontab.managers.DataManager;
import io.github.bycubed7.mmontab.managers.badge.BadgeManager;

public class Reincarnate extends Action {

	static Integer percentage;

	public Reincarnate(MMOnTAB MMOplugin) {
		super("reincarnate", MMOplugin);
		plugin.getCommand(name).setExecutor(this);

		percentage = ConfigManager.igetInt("reincarnatePercent");
	}

	protected ActionFailed approved(Player player, String[] args) {

		// There should be at least one argument
		if (args.length < 1)
			return ActionFailed.ARGUMENTLENGTH;

		// Check to see whether the args are in the correct format.
		String skillName = args[0];
		if (!SkillAPI.getSkills().contains(skillName.toUpperCase())) {
			Debug.Tell(player, String.format("%s is not a valid skill! The skills are: %s", skillName,
					SkillAPI.getSkills().toString()));

			return ActionFailed.OTHER;
		}

		// Get McMMO data
		PrimarySkillType skill = PrimarySkillType.valueOf(skillName.toUpperCase());
		McMMOPlayer mmoPlayer = UserManager.getPlayer(player);

		// The skill in question must be maxed
		boolean isSkillMaxed = mmoPlayer.hasReachedLevelCap(skill);

		// - Unless the "force" modifer is used
		// and the player has permission to force
		boolean isForced = Arrays.stream(args).anyMatch(arg -> arg.equalsIgnoreCase("force"));
		boolean canForce = player.hasPermission(prefix + ".*") || player.hasPermission(prefix + ".force");

		if (isForced && !canForce) {
			return ActionFailed.NOPERMISSION;
		} else if (!isSkillMaxed) {
			Debug.Tell(player, String.format("%s is not maxed!", skillName));
			return ActionFailed.OTHER;
		}

		if (DataManager.getBadgeLevel(player.getUniqueId(), skill.name()) != 1) {
			Debug.Tell(player, "You've already reincarnated this skill!");
			return ActionFailed.OTHER;
		}

		return ActionFailed.NONE;
	}

	@Override
	protected boolean execute(Player player, String[] args) {

		// Note: This does not check whether the player has already reincarnated.

		PrimarySkillType skill = PrimarySkillType.valueOf(args[0].toUpperCase());
		// McMMOPlayer mmoPlayer = UserManager.getPlayer(player);

		// Careful: This resets the players level
		int newLevel = (int) (ExperienceAPI.getLevel(player, skill) * (percentage / 100));
		ExperienceAPI.setLevel(player, skill.name(), newLevel);

		Debug.Tell(player, String.format("Reincarnated %s!", skill.name()));

		// Increase Badge Level
		DataManager.incrementBadgeLevel(player.getUniqueId(), skill.name());
		BadgeManager.update(player);

		return true;
	}

}
