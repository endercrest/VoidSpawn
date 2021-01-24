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

    public void onEnable(){
        /* if[PROD] */
        Metrics metrics = new Metrics(this, 3514);
        /* end[PROD] */

        loadConfiguration();
        ConfigManager.getInstance().setUp(this);
        TeleportManager.getInstance().setUp(this);
        ModeManager.getInstance().setUp(this);
        DetectorManager.getInstance().setUp();
        getServer().getPluginManager().registerEvents(new VoidListener(this), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        PluginCommand command = getCommand("voidspawn");
        CommandHandler commandHandler = new CommandHandler(this);
        command.setExecutor(commandHandler);
        command.setTabCompleter(new VoidSpawnTabCompleter(commandHandler));

        // This is a small hack to allow for pre-1.13 servers to run this version.
        // It should be a temporary measure and at some point VoidSpawn should just
        // disable itself if running on an unsupported version. But that will come
        // another day.
        boolean compatibilityMode = false;
        try {
            Class.forName("org.bukkit.Tag");
        } catch (ClassNotFoundException e) {
            compatibilityMode = true;
            log("&cWARNING: Unsupported version of Spigot detected!");
            log("&cWARNING: Enabling incompatibility mode, there is no guarantee this mode");
            log("&cWARNING: will work properly. This also disables some features. Bugs on");
            log("&cWARNING: this version may not be fixed.");
        }

        Bukkit.getScheduler().runTaskTimer(this, new TouchTracker(compatibilityMode), 5, 5);

        log("&ev" + getDescription().getVersion() + " by EnderCrest enabled");
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