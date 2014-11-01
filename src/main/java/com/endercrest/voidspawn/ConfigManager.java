package com.endercrest.voidspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    public static ConfigManager getInstance(){
        return instance;
    }

    public void setUp(VoidSpawn plugin){
        ConfigManager.plugin = plugin;
        worldFile = new File(plugin.getDataFolder(), "worlds.yml");
        if(!isFileCreated()){
            createFile();
        }
        config = YamlConfiguration.loadConfiguration(worldFile);
    }

    /**
     * Checks if world has been set in config
     * @param world The World Name
     * @return boolean
     */
    public boolean isWorldSpawnSet(String world){
        if(isSet(world))
            if(isSet(world + ".x"))
                if(isSet(world + ".y"))
                    if(isSet(world + ".z"))
                        if(isSet(world + ".pitch"))
                            if(isSet(world + ".yaw"))
                                if(isSet(world + ".world"))
                                    return true;
        return false;
    }

    public void reloadConfig(){
        worldFile = new File(plugin.getDataFolder(), "worlds.yml");
        if(!isFileCreated()){
            createFile();
        }
        config = YamlConfiguration.loadConfiguration(worldFile);
    }

    public void setMode(String world, String mode){
        if(mode.equalsIgnoreCase("none")){
            set(world + ".mode", null);
            return;
        }
        set(world + ".mode", mode);
    }

    public String getMode(String world){
        return getString(world + ".mode");
    }

    public boolean isModeSet(String world){
        return isSet(world + ".mode");
    }

    public void setSpawn(String world, Player player){
        Location loc = player.getLocation();
        set(world + ".x", loc.getX());
        set(world + ".y", loc.getY());
        set(world + ".z", loc.getZ());
        set(world + ".pitch", loc.getPitch());
        set(world + ".yaw", loc.getYaw());
        set(world + ".world", loc.getWorld().getName());
        saveConfig();
    }

    public void setSpawn(Player player){
        Location loc = player.getLocation();
        String world = loc.getWorld().getName();
        set(world + ".x", loc.getX());
        set(world + ".y", loc.getY());
        set(world + ".z", loc.getZ());
        set(world + ".pitch", loc.getPitch());
        set(world + ".yaw", loc.getYaw());
        set(world + ".world", loc.getWorld().getName());
        saveConfig();
    }

    public void removeSpawn(Player player){
        String world = player.getWorld().getName();
        set(world, null);
        saveConfig();
    }

    public void removeSpawn(String world, Player player){
        set(world, player);
        saveConfig();
    }

    public boolean isFileCreated(){
        return worldFile.exists();
    }

    public void createFile(){
        try {
            worldFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isSet(String path){
        return config.isSet(path);
    }

    public double getDouble(String path){
        return config.getDouble(path);
    }

    public float getFloat(String path){
        return (float)config.getDouble(path);
    }

    public String getString(String path){
        return config.getString(path);
    }

    public void set(String path, Object obj){
        config.set(path, obj);
    }

    public void saveConfig(){
        try {
            config.save(worldFile);
        } catch (IOException e) {
            plugin.log("&4Could not save worldFile");
            e.printStackTrace();
        }
    }

}
