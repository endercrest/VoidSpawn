package com.endercrest.voidspawn;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class VoidSpawn extends JavaPlugin {

    public static String prefix = "[&6VS&f] ";
    public static boolean IslandWorld = false;

    @Override
    public void onEnable(){
        loadDependencies();
        loadConfiguration();
        ConfigManager.getInstance().setUp(this);
        TeleportManager.getInstance().setUp(this);
        ModeManager.getInstance().setUp();
        getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        getCommand("voidspawn").setExecutor(new CommandHandler(this));
        log("&ev" + this.getDescription().getVersion() + " by EnderCrest enabled");
    }

    private void loadDependencies(){
        PluginManager pm = Bukkit.getPluginManager();
        if(pm.isPluginEnabled("IslandWorld")){
            log("&eIslandWorld Found. Initializing Support");
            IslandWorld = true;
            log("&eIslandWorld Support Initialized.");
        }else{
            log("&eIslandWorld Not Found. Disabling IslandWorld Support.");
        }
    }

    @Override
    public void onDisable(){
        log("&ev" + this.getDescription().getVersion() + " saving config");
        ConfigManager.getInstance().saveConfig();
        log("&ev" + this.getDescription().getVersion() + " disabled");
    }

    /**
     * Load Configuration
     */
    public void loadConfiguration(){
        if(!getConfig().contains("color-logs")){
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

    public static boolean isValidWorld(String worldName){
        for(World world: Bukkit.getWorlds()){
            if(world.getName().equalsIgnoreCase(worldName)){
                return true;
            }
        }
        return false;
    }

    public static boolean isValidMode(String mode){
        if(mode.equalsIgnoreCase("Spawn") || mode.equalsIgnoreCase("Touch") || mode.equalsIgnoreCase("None")){
            return true;
        }if(IslandWorld){
            if(mode.equalsIgnoreCase("Island")){
                return true;
            }
        }
        return false;
    }


}
