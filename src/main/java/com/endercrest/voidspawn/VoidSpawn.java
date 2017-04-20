package com.endercrest.voidspawn;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidSpawn extends JavaPlugin {
    public static String prefix = "[&6VS&f] ";
    static boolean IslandWorld = false;
    static boolean ASkyBlock = false;
    static boolean USkyBlock = false;
    //TODO Relocate this hashmap
    public HashMap<UUID, Boolean> Toggle;

    public void onEnable() {
        loadDependencies();
        loadConfiguration();
        ConfigManager.getInstance().setUp(this);
        TeleportManager.getInstance().setUp(this);
        ModeManager.getInstance().setUp();
        getServer().getPluginManager().registerEvents(new MoveListener(this), this);
        getCommand("voidspawn").setExecutor(new CommandHandler(this));
        log("&ev" + getDescription().getVersion() + " by EnderCrest enabled");
        Toggle = new HashMap<UUID, Boolean>();
    }

    private void loadDependencies() {
        PluginManager pm = Bukkit.getPluginManager();
        if(pm.isPluginEnabled("IslandWorld")) {
            log("&eIslandWorld found, initializing support.");
            IslandWorld = true;
            log("&eIslandWorld support initialized.");
        }

        if(pm.isPluginEnabled("ASkyBlock")) {
            log("&eASkyBlock found, initializing support.");
            ASkyBlock = true;
            log("&eASkyBlock support initialized.");
        }

        if(pm.isPluginEnabled("uSkyBlock")) {
            log("&eUSkyBlock found, initializing support.");
            USkyBlock = true;
            log("&eUSkyBlock support initialized.");
        }

        if(!IslandWorld && !ASkyBlock && !USkyBlock) {
            log("&eNo SkyBlock plugins found, disabling island mode support.");
        }
    }

    public void onDisable() {
        log("&ev" + getDescription().getVersion() + " saving config");
        ConfigManager.getInstance().saveConfig();
        log("&ev" + getDescription().getVersion() + " disabled");
    }

    public void loadConfiguration() {
        if (!getConfig().contains("color-logs")) {
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
    public static String colorize(String str) {
        return str.replaceAll("(?i)&([a-f0-9k-or])", "\\u00a7$1");
    }

    /**
     * Sends Messages to Console
     * @param obj The Obj(Message)
     */
    public void log(Object obj) {
        if (getConfig().getBoolean("color-logs", true)) {
            getServer().getConsoleSender().sendMessage(colorize("&3[&d" + getName() + "&3] &r" + obj));
        } else {
            Bukkit.getLogger().log(Level.INFO, "[" + getName() + "] " + colorize((String) obj).replaceAll("(?)\\u00a7([a-f0-9k-or])", ""));
        }
    }

    /**
     * Checks if the selected world is a valid world.
     * @param worldName The world name that will be checked.
     * @return True if the world does not return null.
     */
    public static boolean isValidWorld(String worldName) {
        return Bukkit.getWorld(worldName) != null;
    }
}