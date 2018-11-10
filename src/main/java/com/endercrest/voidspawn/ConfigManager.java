package com.endercrest.voidspawn;

import com.endercrest.voidspawn.utils.WorldUtil;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ConfigManager {
    private VoidSpawn plugin;
    private static ConfigManager instance = new ConfigManager();
    private File worldFile;
    private FileConfiguration config;
    private final int CURRENT_VERSION = 1;

    /**
     * Get the running instance of the ConfigManager
     *
     * @return The ConfigManager
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
        this.plugin = plugin;
        worldFile = new File(plugin.getDataFolder(), "worlds.yml");
        boolean isCreated = isFileCreated();
        if(!isCreated){
            createFile();
        }
        config = YamlConfiguration.loadConfiguration(worldFile);

        if(!isCreated){
            config.set("version", CURRENT_VERSION);
        }

        //Run Migration
        migrate();
    }

    /**
     * Migrate the config to new versions.
     */
    private void migrate(){
        migrateV1();

        saveConfig();
    }

    public File getWorldFile() {
        return worldFile;
    }

    /**
     * Migrate the config to version 1 from 0.
     * <p>
     * This migration involves converting names to safe names that do not contain spaces.
     */
    private void migrateV1(){
        if(!config.isSet("version")){
            plugin.log("Converting world.yml to version 1");
            config.set("version", 1);

            ConfigurationSection section = config.getRoot();
            if(section != null){
                for(String key : section.getKeys(false)){
                    //Convert world names into safe world names.
                    if((!key.equalsIgnoreCase("version")) && (!key.equals(WorldUtil.configSafe(key)))){
                        config.set(WorldUtil.configSafe(key), config.get(key));
                        config.set(key, null);
                    }
                }
            }

            plugin.log("Version 1 conversion complete.");
        }
    }

    /**
     * Checks if world has been set in config
     *
     * @param world The world name
     * @return boolean whether the world is set in the config.
     */
    public boolean isWorldSpawnSet(String world){
        world = WorldUtil.configSafe(world);

        return (isSet(world)) && (isSet(world + ".spawn.x")) && (isSet(world + ".spawn.y"))
                && (isSet(world + ".spawn.z")) && (isSet(world + ".spawn.pitch"))
                && (isSet(world + ".spawn.yaw")) && (isSet(world + ".spawn.world"));
    }

    /**
     * Reloads the config from the original file. DOES NOT SAVE BEFORE RELOADING.
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
     * @param mode  The mode for teleporting.
     */
    public void setMode(String world, String mode){
        world = WorldUtil.configSafe(world);

        if(mode.equalsIgnoreCase("none")){
            set(world + ".mode", null);
            return;
        }
        if((mode.equalsIgnoreCase("command")) && (!isSet(world + ".command"))){
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
    public String getMode(String world){
        world = WorldUtil.configSafe(world);

        return getString(world + ".mode", "");
    }

    /**
     * Checks whether anything is set for the mode in a particular world.
     *
     * @param world The wolrd to be checked.
     * @return true if world has a mode set. Does not verify whether mode is valid.
     */
    public boolean isModeSet(String world){
        world = WorldUtil.configSafe(world);

        return isSet(world + ".mode");
    }

    /**
     * Checks if the specified world has a sound set. This just requires the name of a sound to be set.
     * This also doesn't check if it is a valid sound either.
     * @param world The world to be checked.
     * @return true if a world has a sound set. Does not verify that the sound is valid.
     */
    public boolean isSoundSet(String world){
        world = WorldUtil.configSafe(world);

        return isSet(world + ".sound.name");
    }

    /**
     * Sets the sound for the specific world.
     * @param world The world
     * @param sound The sound, does not require to be a valid world.
     */
    public void setSound(String world, String sound){
        world = WorldUtil.configSafe(world);

        set(world+".sound.name", sound);
    }

    /**
     * Get the sound for a world.
     * @param world The world to retrieve the value from.
     * @return A string with the name of the sound. Does not validate and returns null if no sound is set.
     */
    public String getSound(String world){
        world = WorldUtil.configSafe(world);

        return getString(world+".sound.name", null);
    }

    /**
     * Removes the sounds from the world's config. As well as removing the value and pitch values.
     * @param world The world to remove the sound from.
     */
    public void removeSound(String world){
        world = WorldUtil.configSafe(world);

        set(world+".sound", null);
    }

    /**
     * Set the volume of a sound.
     * @param world The world
     * @param value THe value, does not validate, but should be between 0 - 1
     */
    public void setVolume(String world, float value){
        world = WorldUtil.configSafe(world);

        set(world+".sound.volume", value);
    }

    /**
     * Set the pitch of the sound.
     * @param world The world
     * @param value The value, does not validate, but should be between 0 - 2
     */
    public void setPitch(String world, float value){
        world = WorldUtil.configSafe(world);

        set(world+".sound.pitch", value);
    }

    /**
     * Get the volume of the sound for the world.
     * @param world The world.
     * @return Returns the volume set or defaults to 1.0
     */
    public float getVolume(String world){
        world = WorldUtil.configSafe(world);

        return getFloat(world+".sound.volume", 1f);
    }

    /**
     * Get the pitch of the sound for the world.
     * @param world The world
     * @return Returns the pitch set or defaults to 1.0
     */
    public float getPitch(String world){
        world = WorldUtil.configSafe(world);

        return getFloat(world+"sound.pitch", 1f);
    }

    /**
     * Set spawn for a specific world at the location of the specified player.
     *
     * @param player The player who is setting the location.
     * @param world  THe world in which the spawn is being set for.
     */
    public void setSpawn(Player player, String world){
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

    /**
     * Removes the spawn of a world based on the world name.
     *
     * @param world The world name.
     */
    public void removeSpawn(String world){
        world = WorldUtil.configSafe(world);
        set(world + ".spawn", null);
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
        try{
            worldFile.createNewFile();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Update the keep inventory setting.
     *
     * @param bool  The updated boolean
     * @param world The world.
     */
    public void setKeepInventory(boolean bool, String world){
        world = WorldUtil.configSafe(world);

        set(world + ".keep_inventory", bool);
        saveConfig();
    }

    /**
     * Get the value of keep inventory.
     *
     * @param world The world.
     * @return defaults to true if setting is not found.
     */
    public boolean getKeepInventory(String world){
        world = WorldUtil.configSafe(world);

        return getBoolean(world + ".keep_inventory", true);
    }

    /**
     * Update the hybrid setting.
     *
     * @param bool  The update boolean
     * @param world The world.
     */
    public void setHybrid(boolean bool, String world){
        world = WorldUtil.configSafe(world);

        set(world + ".hybrid", bool);
        saveConfig();
    }

    /**
     * Get the value of the hybrid mode.
     *
     * @param world The world
     * @return defaults to false if setting is not found.
     */
    public boolean isHybrid(String world){
        world = WorldUtil.configSafe(world);

        return getBoolean(world + ".hybrid", false);
    }

    /**
     * Set the teleport message for the specified world.
     *
     * @param message The message that will be set.
     * @param world   The world being set for.
     */
    public void setMessage(String message, String world){
        world = WorldUtil.configSafe(world);

        set(world + ".message", message);
        saveConfig();
    }

    /**
     * Removes the teleport message for the specified world.
     *
     * @param world The world that the message will be removed from.
     */
    public void removeMessage(String world){
        world = WorldUtil.configSafe(world);

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
        world = WorldUtil.configSafe(world);

        return getString(world + ".message", "");
    }

    /**
     * Set the offset for a world, this will move the teleportation zone downward.
     * <p>
     * ie, offset of 2 will move the teleportation zone 2 blocks below the start of the void.
     *
     * @param offset The offset
     * @param world  The world that the offset is being sent for.
     */
    public void setOffset(int offset, String world){
        world = WorldUtil.configSafe(world);

        set(world + ".offset", offset);
        saveConfig();
    }

    /**
     * Return the offset for a specific world.
     *
     * @param world The world.
     * @return the offset, will return -1 if it is not set.
     */
    public int getOffSet(String world){
        world = WorldUtil.configSafe(world);

        return getInt(world + ".offset", -1);
    }

    /**
     * Set the command for a specific world.
     *
     * @param command The command(s) to be set for the world. Each command should be separated by ';'
     * @param world   The world.
     */
    public void setCommand(String command, String world){
        world = WorldUtil.configSafe(world);

        set(world + ".command", command);
        saveConfig();
    }

    /**
     * Set the detector value for a world, this method doesn't validate whether it is a valid detector.
     * @param detector The detector value to set.
     * @param world The world.
     */
    public void setDetector(String detector, String world){
        world = WorldUtil.configSafe(world);

        set(world + ".detector", detector);
        saveConfig();
    }

    /**
     * Gets the detector set for a world. If it is not set, the value will return the default detector
     * defined in DetectorManager.
     * @param world The world
     * @return The string name of the detector or defaults to default value set in DetectorManager.
     */
    public String getDetector(String world){
        world = WorldUtil.configSafe(world);

        String detector = getString(world+".detector", "");
        return !detector.isEmpty() ? detector : DetectorManager.getInstance().getDefaultDetectorName();
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
     * Get the double at the specified path.
     *
     * @param path THe YAML path.
     * @return double if it exists.
     */
    public double getDouble(String path, double def){
        return config.getDouble(path, def);
    }

    /**
     * Get the float at the specified path.
     *
     * @param path THe YAML path.
     * @return float if it exists.
     */
    public float getFloat(String path, float def){
        return (float) config.getDouble(path, def);
    }

    /**
     * Get the string at the specified path.
     *
     * @param path THe YAML path.
     * @return string if it exists.
     */
    public String getString(String path, String def){
        return config.getString(path, def);
    }

    /**
     * Get the boolean at the specified path.
     *
     * @param path THe YAML path.
     * @return boolean if it exists.
     */
    public boolean getBoolean(String path, boolean def){
        return config.getBoolean(path, def);
    }

    /**
     * Get the integer at the specified path.
     *
     * @param path THe YAML path.
     * @return integer if it exists.
     */
    public int getInt(String path, int def){
        return config.getInt(path, def);
    }

    /**
     * Set the path in the world data file with the desired value.
     *
     * @param path The path in the YAML file.
     * @param obj  The object being saved into the file.
     */
    private void set(String path, Object obj){
        config.set(path, obj);
    }

    /**
     * Save the world data file.
     */
    public void saveConfig(){
        try{
            config.save(worldFile);
        } catch(IOException e){
            plugin.log("&4Could not save worldFile");
            e.printStackTrace();
        }
    }
}