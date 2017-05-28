package com.endercrest.voidspawn;

import com.endercrest.voidspawn.commands.*;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private VoidSpawn plugin;
    private HashMap<String, SubCommand> commands;

    public CommandHandler(VoidSpawn plugin){
        this.plugin = plugin;
        commands = new HashMap<String, SubCommand>();
        loadCommands();
    }

    /**
     * Load the commands into the HashMap that makes it accessible to players.
     */
    private void loadCommands(){
        commands.put("set", new Set());
        commands.put("remove", new Remove());
        commands.put("reload", new Reload());
        commands.put("modes", new Modes());
        commands.put("mode", new Mode());
        commands.put("help", new Help(commands));
        commands.put("message", new Message());
        commands.put("offset", new Offset());
        commands.put("command", new com.endercrest.voidspawn.commands.Command());
        commands.put("keepinventory", new KeepInventory());
        commands.put("hybrid", new Hybrid());
        commands.put("toggle", new Toggle());
        commands.put("detector", new Detector());
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args){
        if(!(cs instanceof Player)){
            if((args.length >= 1) && (args[0].equalsIgnoreCase("reload"))){
                ConfigManager.getInstance().reloadConfig();
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&6Plugin Reloaded"));
                return true;
            }

            cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cOnly Players can use these commands"));
            return false;
        }
        if(cmd.getName().equalsIgnoreCase("voidspawn")){
            if((args == null) || (args.length < 1)){
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Version &6" + plugin.getDescription().getVersion() + "&f by &6EnderCrest"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
                return true;
            }
            String sub = args[0].toLowerCase();
            if(!commands.containsKey(sub)){
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat command does not exist"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
                return true;
            }
            try{
                commands.get(sub).onCommand((Player) cs, args);
            }catch(Exception e){
                e.printStackTrace();
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThere was an error"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
            }
        }
        return false;
    }
}