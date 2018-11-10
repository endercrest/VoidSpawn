package com.endercrest.voidspawn;

import java.util.logging.Level;

import com.endercrest.voidspawn.commands.VoidSpawnTabCompleter;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidSpawn extends JavaPlugin {
    public static String prefix = "[&6VS&f] ";
    public static boolean IslandWorld = false;
    public static boolean ASkyBlock = false;
    public static boolean BentoBox = false;
    public static boolean USkyBlock = false;

    public void onEnable(){
        /* if[PROD] */
        Metrics metrics = new Metrics(this);
        /* end[PROD] */

        loadDependencies();
        loadConfiguration();
        ConfigManager.getInstance().setUp(this);
        TeleportManager.getInstance().setUp(this);
        ModeManager.getInstance().setUp(this);
        DetectorManager.getInstance().setUp();
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
        getServer().getPluginManager().registerEvents(new VoidListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        PluginCommand command = getCommand("voidspawn");
        CommandHandler commandHandler = new CommandHandler(this);
        command.setExecutor(commandHandler);
        command.setTabCompleter(new VoidSpawnTabCompleter(commandHandler));

        log("&ev" + getDescription().getVersion() + " by EnderCrest enabled");
    }

    private void loadDependencies(){
        PluginManager pm = Bukkit.getPluginManager();
        if(pm.isPluginEnabled("IslandWorld")){
            log("&eIslandWorld found, initializing support.");
            IslandWorld = true;
            log("&eIslandWorld support initialized.");
        }

        if(pm.isPluginEnabled("ASkyBlock")){
            log("&eASkyBlock found, initializing support.");
            log("&cASkyBlock has been deprecated, ASkyBlock has been discontinued and it is recommended to switch to BentoBox");
            ASkyBlock = true;
            log("&eASkyBlock support initialized.");
        }

        if(pm.isPluginEnabled("BentoBox")){
            log("&eBentoBox found, initializing support.");
            BentoBox = true;
            log("&eBentoBox support initialized.");
        }

        if(pm.isPluginEnabled("uSkyBlock")){
            log("&eUSkyBlock found, initializing support.");
            USkyBlock = true;
            log("&eUSkyBlock support initialized.");
        }

        if(!IslandWorld && !ASkyBlock && !BentoBox && !USkyBlock){
            log("&eNo SkyBlock plugins found, disabling island mode support.");
        }
    }

    public void onDisable(){
        log("&ev" + getDescription().getVersion() + " saving config");
        ConfigManager.getInstance().saveConfig();
        log("&ev" + getDescription().getVersion() + " disabled");
    }

    public void loadConfiguration(){
        if(!getConfig().contains("color-logs")){
            getConfig().addDefault("color-logs", true);
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Sends Messages to Console
     *
     * @param obj The Obj(Message)
     */
    public void log(Object obj){
        if(getConfig().getBoolean("color-logs", true)){
            getServer().getConsoleSender().sendMessage(MessageUtil.colorize("&3[&d" + getName() + "&3] &r" + obj));
        }else{
            Bukkit.getLogger().log(Level.INFO, "[" + getName() + "] " + MessageUtil.colorize((String) obj).replaceAll("(?)\u00a7([a-f0-9k-or])", ""));
        }
    }

}