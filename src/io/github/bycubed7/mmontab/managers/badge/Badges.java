package io.github.bycubed7.mmontab.managers.badge;

import java.io.Serializable;
import java.util.HashMap;

public class Badges implements Serializable {
	private static final long serialVersionUID = 1750005742264695868L;

	private HashMap<String, Integer> badges;

	final public void put(String badgeName, Integer number) {
		badges.put(badgeName, number);
	}

	final public Integer getLevel(String badgeName) {
		if (badges.containsKey(badgeName))
			return badges.get(badgeName);
		return 0;
	}

	public Badges() {
		badges = new HashMap<>();
	}

}
