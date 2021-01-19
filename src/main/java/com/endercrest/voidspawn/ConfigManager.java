package com.endercrest.voidspawn;

import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.options.OptionIdentifier;
import com.endercrest.voidspawn.utils.WorldUtil;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ConfigManager {
    private VoidSpawn plugin;
    private static final ConfigManager instance = new ConfigManager();
    private File worldFile;
    private FileConfiguration config;
    private final int CURRENT_VERSION = 2;

    /**
     * Get the running instance of the ConfigManager
     *
     * @return The ConfigManager
     */
    public static ConfigManager getInstance() {
        return instance;
    }

    /**
     * Setup the ConfigManager instance. Should only be called on startup.
     *
     * @param plugin VoidSpawn Plugin.
     */
    public void setUp(VoidSpawn plugin) {
        this.plugin = plugin;
        worldFile = new File(plugin.getDataFolder(), "worlds.yml");
        boolean isCreated = isFileCreated();
        if (!isCreated) {
            createFile();
        }
        config = YamlConfiguration.loadConfiguration(worldFile);

        if (!isCreated) {
            config.set("version", CURRENT_VERSION);
        }

        //Run Migration
        migrate();
    }

    /**
     * Migrate the config to new versions.
     */
    private void migrate() {
        migrateV1();
        migrateV2();

        saveConfig();
    }

    /**
     * Migrate the config to version 1 from 0.
     * <p>
     * This migration involves converting names to safe names that do not contain spaces.
     */
    private void migrateV1() {
        if (!config.isSet("version")) {
            plugin.log("Converting worlds.yml to version 1");
            config.set("version", 1);

            ConfigurationSection section = config.getRoot();
            if (section != null) {
                for (String key: section.getKeys(false)) {
                    //Convert world names into safe world names.
                    if ((!key.equalsIgnoreCase("version")) && (!key.equals(WorldUtil.configSafe(key)))) {
                        config.set(WorldUtil.configSafe(key), config.get(key));
                        config.set(key, null);
                    }
                }
            }

            plugin.log("Version 1 conversion complete.");
        }
    }

    private void migrateV2() {
        if (config.getInt("version", 0) >= CURRENT_VERSION)
            return;

        long time = Instant.now().toEpochMilli();

        String newName = String.format("worlds.%s.yml", time);
        File file = new File(plugin.getDataFolder(), newName);
        plugin.log(String.format("Backing up worlds.yml to %s", newName));
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.log("Failed to backup worlds.yml");
            e.printStackTrace();
            return;
        }

        plugin.log("Converting world.yml to version 2");
        config.set("version", 2);

        for (String key: config.getKeys(false)) {
            if (!config.isConfigurationSection(key))
                continue;
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section == null)
                continue;

            section.set("options." + BaseMode.OPTION_OFFSET.getName(), section.get("offset"));
            section.set("options." + BaseMode.OPTION_MESSAGE.getName(), section.get("message"));
            section.set("options." + BaseMode.OPTION_HYBRID.getName(), section.get("hybrid"));
            section.set("options." + BaseMode.OPTION_SOUND.getName(), section.get("sound.name"));
            section.set("options." + BaseMode.OPTION_SOUND_VOLUME.getName(), section.get("sound.volume"));
            section.set("options." + BaseMode.OPTION_SOUND_PITCH.getName(), section.get("sound.pitch"));
            section.set("options." + BaseMode.OPTION_KEEP_INVENTORY.getName(), section.get("keep_inventory"));
            section.set("options." + BaseMode.OPTION_COMMAND.getName(), section.get("command"));

            section.set("offset", null);
            section.set("message", null);
            section.set("hybrid", null);
            section.set("sound.name", null);
            section.set("sound.volume", null);
            section.set("sound.pitch", null);
            section.set("keep_inventory", null);
            section.set("command", null);
        }

        plugin.log("Version 2 conversion complete.");
    }

    /**
     * Checks if world has been set in config
     *
     * @param world The world name
     * @return boolean whether the world is set in the config.
     */
    public boolean isWorldSpawnSet(String world) {
        world = WorldUtil.configSafe(world);

        return (isSet(world)) && (isSet(world + ".spawn.x")) && (isSet(world + ".spawn.y"))
                && (isSet(world + ".spawn.z")) && (isSet(world + ".spawn.pitch"))
                && (isSet(world + ".spawn.yaw")) && (isSet(world + ".spawn.world"));
    }

    /**
     * Reloads the config from the original file. DOES NOT SAVE BEFORE RELOADING.
     */
    public void reloadConfig() {
        worldFile = new File(plugin.getDataFolder(), "worlds.yml");
        if (!isFileCreated()) {
            createFile();
        }
        config = YamlConfiguration.loadConfiguration(worldFile);
    }

    /**
     * Set the spawn mode for the specified world.
     *
     * @param world The world being set.
     * @param mode  The mode for teleporting.
     */
    public void setMode(String world, String mode) {
        world = WorldUtil.configSafe(world);

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

    /**
     * Get the current mode for the specified world.
     *
     * @param world The world that the mode is set for.
     * @return String name of the mode or empty string if nothing is found.
     */
    public String getMode(String world) {
        world = WorldUtil.configSafe(world);

        return getString(world + ".mode", "");
    }

    /**
     * Checks whether anything is set for the mode in a particular world.
     *
     * @param world The wolrd to be checked.
     * @return true if world has a mode set. Does not verify whether mode is valid.
     */
    public boolean isModeSet(String world) {
        world = WorldUtil.configSafe(world);

        return isSet(world + ".mode");
    }

    /**
     * Set spawn for a specific world at the location of the specified player.
     *
     * @param player The player who is setting the location.
     * @param world  THe world in which the spawn is being set for.
     */
    public void setSpawn(Player player, String world) {
        world = WorldUtil.configSafe(world);

        Location loc = player.getLocation();
        set(world + ".spawn.x", loc.getX());
        set(world + ".spawn.y", loc.getY());
        set(world + ".spawn.z", loc.getZ());
        set(world + ".spawn.pitch", loc.getPitch());
        set(world + ".spawn.yaw", loc.getYaw());
        set(world + ".spawn.world", loc.getWorld().getName());
        saveConfig();
    }

    public Location getSpawn(String world) {
        if(!isWorldSpawnSet(world))
            return null;

        world = WorldUtil.configSafe(world);


        return new Location(Bukkit.getWorld(getString(world + ".spawn.x", null)),
                getDouble(world + ".spawn.x", 0.0),
                getDouble(world + ".spawn.y", 0.0),
                getDouble(world + ".spawn.z", 0.0),
                getFloat(world + ".spawn.pitch", 0.0f),
                getFloat(world + ".spawn.yaw", 0.0f));
    }

    /**
     * Removes the spawn of a world based on the world name.
     *
     * @param world The world name.
     */
    public void removeSpawn(String world) {
        world = WorldUtil.configSafe(world);
        set(world + ".spawn", null);
        saveConfig();
    }

    /**
     * Checks if the world data file exists.
     *
     * @return True if world.yml exists.
     */
    public boolean isFileCreated() {
        return worldFile.exists();
    }

    /**
     * Create the world file.
     */
    public void createFile() {
        try {
            worldFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOption(String world, OptionIdentifier<?> identifier) {
        return getOption(world, identifier.getName());
    }

    public String getOption(String world, String option) {
        world = WorldUtil.configSafe(world);

        return getString(world + ".options." + option, null);
    }

    public void setOption(String world, String options, String value) {
        world = WorldUtil.configSafe(world);

        set(world + ".options." + options, value);
        saveConfig();
    }

    /**
     * Set the detector value for a world, this method doesn't validate whether it is a valid detector.
     *
     * @param detector The detector value to set.
     * @param world    The world.
     */
    public void setDetector(String detector, String world) {
        world = WorldUtil.configSafe(world);

        set(world + ".detector", detector);
        saveConfig();
    }

    /**
     * Gets the detector set for a world. If it is not set, the value will return the default detector
     * defined in DetectorManager.
     *
     * @param world The world
     * @return The string name of the detector or defaults to default value set in DetectorManager.
     */
    public String getDetector(String world) {
        world = WorldUtil.configSafe(world);

        String detector = getString(world + ".detector", "");
        return !detector.isEmpty() ? detector : DetectorManager.getInstance().getDefaultDetectorName();
    }

    /**
     * Checks if the path is set.
     *
     * @param path The YAML path to check.
     * @return True if there is a value assigned to it.
     */
    private boolean isSet(String path) {
        return config.isSet(path);
    }

    /**
     * Get the double at the specified path.
     *
     * @param path THe YAML path.
     * @return double if it exists.
     */
    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    /**
     * Get the float at the specified path.
     *
     * @param path THe YAML path.
     * @return float if it exists.
     */
    public float getFloat(String path, float def) {
        return (float) config.getDouble(path, def);
    }

    /**
     * Get the string at the specified path.
     *
     * @param path THe YAML path.
     * @return string if it exists.
     */
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    /**
     * Get the boolean at the specified path.
     *
     * @param path THe YAML path.
     * @return boolean if it exists.
     */
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }

    /**
     * Get the integer at the specified path.
     *
     * @param path THe YAML path.
     * @return integer if it exists.
     */
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    /**
     * Set the path in the world data file with the desired value.
     *
     * @param path The path in the YAML file.
     * @param obj  The object being saved into the file.
     */
    private void set(String path, Object obj) {
        config.set(path, obj);
    }

    /**
     * Save the world data file.
     */
    public void saveConfig() {
        try {
            config.save(worldFile);
        } catch (IOException e) {
            plugin.log("&4Could not save worldFile");
            e.printStackTrace();
        }
    }
}