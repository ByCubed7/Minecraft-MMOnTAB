package io.github.bycubed7.mmontab.managers.badge;

import java.util.EnumSet;

public enum BadgePlace {
	HIDE, TABPREFIX, TABSUFFIX, BELOWNAME, ABOVENAME;

	public static final EnumSet<BadgePlace> ALL_OPTS = EnumSet.allOf(BadgePlace.class);

	public static final BadgePlace integer(Integer value) {
		return values()[value];
	}
}
