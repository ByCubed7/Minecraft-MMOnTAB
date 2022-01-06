package io.github.bycubed7.mmontab.managers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import io.github.bycubed7.mmontab.managers.badge.Badges;

public class SaveData implements Serializable {
	private static final long serialVersionUID = -4504651822637819059L;

	public HashMap<UUID, Badges> badges;
	public HashMap<String, String> config;

}
