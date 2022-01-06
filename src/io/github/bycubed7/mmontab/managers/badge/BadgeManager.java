package io.github.bycubed7.mmontab.managers.badge;

import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.nossr50.api.SkillAPI;

import io.github.bycubed7.mmontab.Debug;
import io.github.bycubed7.mmontab.managers.ConfigManager;
import io.github.bycubed7.mmontab.managers.DataManager;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.TablistFormatManager;
import me.neznamy.tab.api.team.TeamManager;
import me.neznamy.tab.api.team.UnlimitedNametagManager;

public class BadgeManager {

	static JavaPlugin plugin;

	static Integer badgePlace;

	static Integer badgeCount;
	static boolean badgeGrouping;

	static String maxColor;
	static String reincarnateColor;
	static String maxReincarnateColor;

	static String maxBadge;
	static String reiBadge;
	static String mReBadge;

	static String combatName;
	static String gatherName;
	static String otherName;
	static String combatAbbr;
	static String gatherAbbr;
	static String otherAbbr;

	public BadgeManager(JavaPlugin plugin) {
		BadgeManager.plugin = plugin;

		badgePlace = ConfigManager.igetInt("badgePlace");

		badgeCount = ConfigManager.igetInt("badgeCount");
		badgeGrouping = ConfigManager.igetBool("badgeGrouping");

		maxColor = ConfigManager.igetString("maxColor");
		reincarnateColor = ConfigManager.igetString("reincarnateColor");
		maxReincarnateColor = ConfigManager.igetString("maxReincarnateColor");

		maxBadge = maxColor + ConfigManager.igetString("maxBadge");
		reiBadge = reincarnateColor + ConfigManager.igetString("reincarnateBadge");
		mReBadge = maxReincarnateColor + ConfigManager.igetString("maxReincarnateBadge");

		combatName = ConfigManager.igetString("categories.name.combat");
		gatherName = ConfigManager.igetString("categories.name.gathering");
		otherName = ConfigManager.igetString("categories.name.other");
		combatAbbr = ConfigManager.igetString("categories.abbr.combat");
		gatherAbbr = ConfigManager.igetString("categories.abbr.gathering");
		otherAbbr = ConfigManager.igetString("categories.abbr.other");
	}

	//

	public static void setBadge(Player player, String badgeString) {
		// Tell TABs to update the players badge(s)
		TabAPI tabApi = TabAPI.getInstance();
		TabPlayer tabPlayer = tabApi.getPlayer(player.getUniqueId());

		// if the player isnt loaded
		if (tabPlayer == null || !tabPlayer.isLoaded()) {

			Debug.Log(String.format("[TabPlayer] Can't find %s's TAB to update! Running Task later..",
					player.getDisplayName()));

			// Try and do this later
			new BukkitRunnable() {
				@Override
				public void run() {
					BadgeManager.setBadge(player, badgeString);
				}
			}.runTaskLater(plugin, 1000L); // Delays in ticks

			// Warning: possible async infinite loop if player is never detected.

			// Note: This is just a round-a-bout way of doing async functions
			// without actually using async, fix this design pattern.

			return;
		}

		TablistFormatManager tablistFormatManager = tabApi.getTablistFormatManager();
		TeamManager teamManager = tabApi.getTeamManager();

		BadgePlace place = BadgePlace.integer(badgePlace);
		switch (place) {
		default:
		case HIDE:
			break;

		case TABPREFIX:
			tablistFormatManager.setPrefix(tabPlayer, badgeString);
			break;

		case TABSUFFIX:
			tablistFormatManager.setSuffix(tabPlayer, badgeString);
			break;

		case BELOWNAME:
			if (teamManager instanceof UnlimitedNametagManager) {
				((UnlimitedNametagManager) teamManager).setLine(tabPlayer, "belowname", badgeString);
				Debug.Log("BadgeManager.SetBadge.BELOWNAME :: This may or may not display correctly", ChatColor.YELLOW);
			} else {
				Debug.Log(
						"setBadge.BELOWNAME :: You must enable Unlimited nametags in the TAB config for this to display correctly",
						ChatColor.RED);
			}
			break;

		case ABOVENAME:
			if (teamManager instanceof UnlimitedNametagManager) {
				((UnlimitedNametagManager) teamManager).setLine(tabPlayer, "abovename", badgeString);
				Debug.Log("BadgeManager.SetBadge.ABOVENAME :: This may or may not display correctly", ChatColor.YELLOW);
			} else {
				Debug.Log(
						"setBadge.ABOVENAME :: You must enable Unlimited nametags in the TAB config for this to display correctly",
						ChatColor.RED);
			}
			break;
		}

		return;
	}

	//

	public static void update(Player player) {

		Debug.Log("Updating " + player.getDisplayName());

		//

		Badges badges = DataManager.getBadges(player.getUniqueId());

		//

		String suffix = "";
		Integer currentCount = 0;

		//

		int combatLevel = combatLevel(badges);
		int gatherLevel = gatherLevel(badges);
		int otherLevel = otherLevel(badges);

		if (badgeGrouping) {
			if ((badgeCount == 0 || badgeCount > currentCount) && combatLevel > 0) {
				currentCount++;
				suffix += getBadgeName(combatLevel, badges, combatName, combatAbbr, maxBadge, reiBadge, mReBadge);
			}

			if ((badgeCount == 0 || badgeCount > currentCount) && gatherLevel > 0) {
				currentCount++;
				suffix += getBadgeName(gatherLevel, badges, gatherName, gatherAbbr, maxBadge, reiBadge, mReBadge);
			}

			if ((badgeCount == 0 || badgeCount > currentCount) && otherLevel > 0) {
				currentCount++;
				suffix += getBadgeName(otherLevel, badges, otherName, otherAbbr, maxBadge, reiBadge, mReBadge);
			}
		}

		for (String skill : SkillAPI.getSkills()) {

			// Break if we're going over the limit
			if (badgeCount != 0 && badgeCount < currentCount)
				break;

			if (badgeGrouping) {
				if (combatLevel > 0 && SkillAPI.getCombatSkills().contains(skill))
					continue;
				if (gatherLevel > 0 && SkillAPI.getGatheringSkills().contains(skill))
					continue;
				if (otherLevel > 0 && SkillAPI.getMiscSkills().contains(skill))
					continue;
			}

			Integer level = badges.getLevel(skill);

			String skillName = ConfigManager.igetString("skills.name." + skill.toLowerCase());
			String skillAbbr = ConfigManager.igetString("skills.abbr." + skill.toLowerCase());
			if (level > 0) {
				currentCount++;
				suffix += getBadgeName(level, badges, skillName, skillAbbr, maxBadge, reiBadge, mReBadge);
			}

		}

		//

		setBadge(player, suffix);
	}

	private static String getBadgeName(Integer level, Badges badges, String skillName, String skillAbbr,
			String maxBadge, String reiBadge, String mReBadge) {
		String badgeName = " ";

		switch (level) {
		case 1:
			if (maxBadge != "0")
				badgeName += maxBadge;
			break;
		case 2:
			if (reiBadge != "0")
				badgeName += reiBadge;
			break;
		case 3:
			if (mReBadge != "0")
				badgeName += mReBadge;
			break;
		}

		// '$S' will be replaced with the full name of the skill
		// '$s' will be replaced with a small abbreviation of the skills name
		// '$n' will be replaced with the level of the skill
		return badgeName.replaceAll("&S", skillName).replaceAll("&s", skillAbbr);
	}

	private static Integer combatLevel(Badges badges) {
		return badges.getLevel(SkillAPI.getCombatSkills().stream()
				.sorted((o1, o2) -> badges.getLevel(o1).compareTo(badges.getLevel(o2))).collect(Collectors.toList())
				.get(0));
	}

	private static Integer gatherLevel(Badges badges) {
		return badges.getLevel(SkillAPI.getGatheringSkills().stream()
				.sorted((o1, o2) -> badges.getLevel(o1).compareTo(badges.getLevel(o2))).collect(Collectors.toList())
				.get(0));
	}

	private static Integer otherLevel(Badges badges) {
		return badges.getLevel(
				SkillAPI.getMiscSkills().stream().sorted((o1, o2) -> badges.getLevel(o1).compareTo(badges.getLevel(o2)))
						.collect(Collectors.toList()).get(0));
	}
}
