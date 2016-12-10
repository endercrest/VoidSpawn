package com.endercrest.voidspawn;

import com.endercrest.voidspawn.utils.WorldName;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private static VoidSpawn plugin;
    private static ConfigManager instance = new ConfigManager();
    private File worldFile;
    private FileConfiguration config;
    private final int CURRENT_VERSION = 1;

    /**
     * Get the running instance of the ConfigManager.
     *
     * @return The ConfigManager.
     */
    public static ConfigManager getInstance(){
        return instance;
    }

    /**
     * Setup the ConfigManager instance. Should only be called on startup.
     *
     * @param plugin VoidSpawn Plugin.
     */
    public void setUp(VoidSpawn plugin){
        ConfigManager.plugin = plugin;
        worldFile = new File(plugin.getDataFolder(), "worlds.yml");
        boolean isCreated = isFileCreated();
        if(!isCreated)
            createFile();

        config = YamlConfiguration.loadConfiguration(worldFile);

        if(!isCreated)
            config.set("version", CURRENT_VERSION);

        //Migration
        migrate();
    }

    /**
     * Migrate the config to new versions.
     */
    private void migrate(){
        migrateV1();

        saveConfig();
    }

    /**
     * Migrate the config to version 1 from 0.
     *
     * This migration involves converting names to safe names that do not contains spaces.
     */
    private void migrateV1(){
        if(!config.isSet("version")){
            plugin.log("Converting world.yml to version 1");
            config.set("version", 1);

            ConfigurationSection section = config.getRoot();
            if(section != null) {
                for (String key : section.getKeys(false)) {
                    //Convert world names into safe world names.
                    if (!key.equalsIgnoreCase("version")) {
                        if(!key.equals(WorldName.configSafe(key))) {
                            config.set(WorldName.configSafe(key), config.get(key));
                            config.set(key, null);
                        }
                    }
                }
            }
            plugin.log("Version 1 conversion complete.");
        }
    }

    /**
     * Checks if world has been set in config
     *
     * @param world The World Name
     * @return boolean whether the world is set in the config.
     */
    public boolean isWorldSpawnSet(String world){
        world = WorldName.configSafe(world);

        return isSet(world) && isSet(world + ".spawn.x") && isSet(world + ".spawn.y") && isSet(world + ".spawn.z")
                && isSet(world + ".spawn.pitch") && isSet(world + ".spawn.yaw") && isSet(world + ".spawn.world");
    }

    /**
     * Reloads the config from the orignal file. DOES NOT SAVE BEFORE RELOADING.
     */
    public void reloadConfig(){
        worldFile = new File(plugin.getDataFolder(), "worlds.yml");
        if(!isFileCreated()){
            createFile();
        }
        config = YamlConfiguration.loadConfiguration(worldFile);
    }

    /**
     * Set the spawn mode for the specified world.
     *
     * @param world The world being set.
     * @param mode The mode for teleporting.
     */
    public void setMode(String world, String mode){
        world = WorldName.configSafe(world);

        if(mode.equalsIgnoreCase("none")){
            set(world + ".mode", null);
            return;
        }
        if(mode.equalsIgnoreCase("command")){
            if(!isSet(world + ".command"))
                set(world + ".command", "spawn");
        }
        set(world + ".mode", mode);
        saveConfig();
    }

    /**
     * Get the current mode for the specified world.
     *
     * @param world The world that the mode is set for.
     * @return String name of the mode.
     */
    public String getMode(String world){
        world = WorldName.configSafe(world);

        return getString(world + ".mode");
    }

    /**
     * Checks whether anything is set for the mode in a particular world.
     *
     * @param world The world to be checked.
     * @return true if world has a mode set. Does not verify whether mode is valid.
     */
    public boolean isModeSet(String world){
        world = WorldName.configSafe(world);

        return isSet(world + ".mode");
    }

    /**
     * Set spawn for a specific world at the location of the specified player.
     *
     * @param player The player who is setting the location.
     * @param world The world in which the spawn is being set for.
     */
    public void setSpawn(Player player, String world){
        world = WorldName.configSafe(world);

        Location loc = player.getLocation();
        set(world + ".spawn.x", loc.getX());
        set(world + ".spawn.y", loc.getY());
        set(world + ".spawn.z", loc.getZ());
        set(world + ".spawn.pitch", loc.getPitch());
        set(world + ".spawn.yaw", loc.getYaw());
        set(world + ".spawn.world", loc.getWorld().getName());
        saveConfig();
    }

    /**
     * Removes the spawn of a world based on the player.
     *
     * @param player The player.
     */
    public void removeSpawn(Player player){
        String world = WorldName.configSafe(player.getWorld().getName());
        set(world+".spawn", null);
        saveConfig();
    }

    /**
     * Removes the spawn of a world based on the world name.
     *
     * @param world The world name.
     */
    public void removeSpawn(String world){
        world = WorldName.configSafe(world);
        set(world+".spawn", null);
        saveConfig();
    }

    /**
     * Checks if the world data file exists.
     *
     * @return True if world.yml exists.
     */
    public boolean isFileCreated(){
        return worldFile.exists();
    }

    /**
     * Create the world file.
     */
    public void createFile(){
        try {
            worldFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the teleport message for the specified world.
     *
     * @param message The message that will be set.
     * @param world The world being set for.
     */
    public void setMessage(String message, String world){
        world = WorldName.configSafe(world);

        set(world + ".message", message);
        saveConfig();
    }

    /**
     * Removes the teleport message for the specified world.
     *
     * @param world The world that the message will be removed from.
     */
    public void removeMessage(String world){
        world = WorldName.configSafe(world);

        set(world + ".message", null);
        saveConfig();
    }

    /**
     * Get the message for the specified world.
     *
     * @param world The world to retrieve the message from.
     * @return The message.
     */
    public String getMessage(String world){
        world = WorldName.configSafe(world);

        return getString(world + ".message");
    }

    /**
     * Set the offset for a world, this will move the teleportation zone downward.
     * @param offset The offset, ie 2
     * @param world The world that the offset is being sent for.
     */
    public void setOffset(int offset, String world){
        world = WorldName.configSafe(world);

        set(world +".offset", offset);
        saveConfig();
    }

    /**
     * Return the offset for a specific world.
     * @param world The world.
     * @return the offset of will return -1 if it is not set.
     */
    public int getOffSet(String world){
        world = WorldName.configSafe(world);

        return getInt(world + ".offset");
    }

    /**
     * Checks if the path is set.
     *
     * @param path The YAML path to check.
     * @return True if there is a value assigned to it.
     */
    private boolean isSet(String path){
        return config.isSet(path);
    }

    /**
     * Get the doubled at the specified path.
     *
     * @param path The YAML path.
     * @return double if it exists.
     */
    public double getDouble(String path){
        return config.getDouble(path);
    }

    /**
     * Get the float at the specified path.
     *
     * @param path The YAML path.
     * @return float if it exists.
     */
    public float getFloat(String path){
        return (float)config.getDouble(path);
    }

    /**
     * Get the string at the specified path.
     *
     * @param path The YAML path.
     * @return string if it exists.
     */
    public String getString(String path){
        return config.getString(path, "");
    }

    /**
     * Get the int at the specified path.
     * @param path The YAML path.
     * @return Returns the int or -1 if it does not exist.
     */
    public int getInt(String path){
        return config.getInt(path, -1);
    }

    /**
     * Sets the path in the world data file with the desired value.
     *
     * @param path The path in the YAML file.
     * @param obj The object being saved into the file.
     */
    private void set(String path, Object obj){
        config.set(path, obj);
    }

    /**
     * Saves the world data file.
     */
    public void saveConfig(){
        try {
            config.save(worldFile);
        } catch (IOException e) {
            plugin.log("&4Could not save worldFile");
            e.printStackTrace();
        }
    }

}
