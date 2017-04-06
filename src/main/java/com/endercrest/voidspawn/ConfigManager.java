package com.endercrest.voidspawn;

import com.endercrest.voidspawn.utils.WorldName;
import java.io.File;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ConfigManager {
	private static VoidSpawn plugin;
	private static ConfigManager instance = new ConfigManager();
	private File worldFile;
	private FileConfiguration config;
	private final int CURRENT_VERSION = 1;

	public static ConfigManager getInstance() {
		return instance;
	}

	public void setUp(VoidSpawn plugin) {
		plugin = plugin;
		this.worldFile = new File(plugin.getDataFolder(), "worlds.yml");
		boolean isCreated = isFileCreated();
		if (!isCreated) {
			createFile();
		}
		this.config = YamlConfiguration.loadConfiguration(this.worldFile);

		if (!isCreated) {
			this.config.set("version", Integer.valueOf(1));
		}

		migrate();
	}

	private void migrate() {
		migrateV1();

		saveConfig();
	}

	private void migrateV1() {
		if (!this.config.isSet("version")) {
			plugin.log("Converting world.yml to version 1");
			this.config.set("version", Integer.valueOf(1));

			ConfigurationSection section = this.config.getRoot();
			if (section != null) {
				for (String key : section.getKeys(false)) {
					if ((!key.equalsIgnoreCase("version")) && (!key.equals(WorldName.configSafe(key)))) {
						this.config.set(WorldName.configSafe(key), this.config.get(key));
						this.config.set(key, null);
					}
				}
			}

			plugin.log("Version 1 conversion complete.");
		}
	}

	public boolean isWorldSpawnSet(String world) {
		world = WorldName.configSafe(world);

		return (isSet(world)) && (isSet(world + ".spawn.x")) && (isSet(world + ".spawn.y"))
				&& (isSet(world + ".spawn.z")) && (isSet(world + ".spawn.pitch")) && (isSet(world + ".spawn.yaw"))
				&& (isSet(world + ".spawn.world"));
	}

	public void reloadConfig() {
		this.worldFile = new File(plugin.getDataFolder(), "worlds.yml");
		if (!isFileCreated()) {
			createFile();
		}
		this.config = YamlConfiguration.loadConfiguration(this.worldFile);
	}

	public void setMode(String world, String mode) {
		world = WorldName.configSafe(world);

		if (mode.equalsIgnoreCase("none")) {
			set(world + ".mode", null);
			return;
		}
		if ((mode.equalsIgnoreCase("command")) && (!isSet(world + ".command"))) {
			set(world + ".command", "spawn");
		}
		set(world + ".mode", mode);
		saveConfig();
	}

	public String getMode(String world) {
		world = WorldName.configSafe(world);

		return getString(world + ".mode");
	}

	public boolean isModeSet(String world) {
		world = WorldName.configSafe(world);

		return isSet(world + ".mode");
	}

	public void setSpawn(Player player, String world) {
		world = WorldName.configSafe(world);

		Location loc = player.getLocation();
		set(world + ".spawn.x", Double.valueOf(loc.getX()));
		set(world + ".spawn.y", Double.valueOf(loc.getY()));
		set(world + ".spawn.z", Double.valueOf(loc.getZ()));
		set(world + ".spawn.pitch", Float.valueOf(loc.getPitch()));
		set(world + ".spawn.yaw", Float.valueOf(loc.getYaw()));
		set(world + ".spawn.world", loc.getWorld().getName());
		saveConfig();
	}

	public void removeSpawn(Player player) {
		String world = WorldName.configSafe(player.getWorld().getName());
		set(world + ".spawn", null);
		saveConfig();
	}

	public void removeSpawn(String world) {
		world = WorldName.configSafe(world);
		set(world + ".spawn", null);
		saveConfig();
	}

	public boolean isFileCreated() {
		return this.worldFile.exists();
	}

	public void createFile() {
		try {
			this.worldFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setKeepInventory(boolean bool, String world) {
		world = WorldName.configSafe(world);

		set(world + ".keep_inventory", Boolean.valueOf(bool));
		saveConfig();
	}

	public boolean getKeepInventory(String world) {
		world = WorldName.configSafe(world);

		return this.config.getBoolean(world + ".keep_inventory", true);
	}

	public void setHybrid(boolean bool, String world) {
		world = WorldName.configSafe(world);

		set(world + ".hybrid", Boolean.valueOf(bool));
		saveConfig();
	}

	public boolean isHybrid(String world) {
		world = WorldName.configSafe(world);

		return this.config.getBoolean(world + ".hybrid", false);
	}

	public void setMessage(String message, String world) {
		world = WorldName.configSafe(world);

		set(world + ".message", message);
		saveConfig();
	}

	public void removeMessage(String world) {
		world = WorldName.configSafe(world);

		set(world + ".message", null);
		saveConfig();
	}

	public String getMessage(String world) {
		world = WorldName.configSafe(world);

		return getString(world + ".message");
	}

	public void setOffset(int offset, String world) {
		world = WorldName.configSafe(world);

		set(world + ".offset", Integer.valueOf(offset));
		saveConfig();
	}

	public int getOffSet(String world) {
		world = WorldName.configSafe(world);

		return getInt(world + ".offset");
	}

	public void setCommand(String command, String world) {
		world = WorldName.configSafe(world);

		set(world + ".command", command);
		saveConfig();
	}

	private boolean isSet(String path) {
		return this.config.isSet(path);
	}

	public double getDouble(String path) {
		return this.config.getDouble(path);
	}

	public float getFloat(String path) {
		return (float) this.config.getDouble(path);
	}

	public String getString(String path) {
		return this.config.getString(path, "");
	}

	public boolean getBoolean(String path) {
		return this.config.getBoolean(path, true);
	}

	public int getInt(String path) {
		return this.config.getInt(path, -1);
	}

	private void set(String path, Object obj) {
		this.config.set(path, obj);
	}

	public void saveConfig() {
		try {
			this.config.save(this.worldFile);
		} catch (IOException e) {
			plugin.log("&4Could not save worldFile");
			e.printStackTrace();
		}
	}
}