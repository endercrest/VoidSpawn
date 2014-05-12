package com.endercrest.voidspawn;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Main Class for VoidSpawn
 */
public class VoidSpawn extends JavaPlugin {

    public List<String> disabled_worlds;
    public String config_version = "1.0";

    @Override
    public void onEnable(){
        loadConfiguration();
        getServer().getPluginManager().registerEvents(new MoveEvent(this), this);
        log("&ev" + this.getDescription().getVersion() + " by EnderCrest");
        getCommand("void").setExecutor(new CmdVoid(this));
    }

    @Override
    public void onDisable(){
        log("&ePlugin Disabled");
    }

    /**
     * Load/Add Configuration
     */
    public void loadConfiguration(){
        if(!getConfig().contains("version")){
            getConfig().addDefault("config_version", config_version);
        }
        if (!getConfig().contains("color-logs")){
            getConfig().addDefault("color-logs", true);
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Add Color to Messages
     * @param str The String
     * @return Coloured String
     */
    public static String colorize(String str){
        return str.replaceAll("(?i)&([a-f0-9k-or])", "\u00a7$1");
    }

    /**
     * Sends Messages to Console
     * @param obj The Obj(Message)
     */
    public void log(Object obj){
        if(getConfig().getBoolean("color-logs", true)){
            getServer().getConsoleSender().sendMessage(colorize("&3[&d" + getName() + "&3] &r" + obj));
        }else{
            Bukkit.getLogger().log(Level.INFO, "[" + getName() + "] " + (colorize((String) obj)).replaceAll("(?)\u00a7([a-f0-9k-or])", ""));
        }
    }
}
