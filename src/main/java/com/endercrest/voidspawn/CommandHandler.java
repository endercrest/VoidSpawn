package com.endercrest.voidspawn;

import com.endercrest.voidspawn.commands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandHandler implements CommandExecutor {

    private VoidSpawn plugin;
    private HashMap<String, SubCommand> commands;

    public CommandHandler(VoidSpawn plugin){
        this.plugin = plugin;
        commands = new HashMap<String, SubCommand>();
        loadCommands();
    }

    private void loadCommands(){
        commands.put("set", new Set());
        commands.put("remove", new Remove());
        commands.put("reload", new Reload());
        commands.put("mode", new Mode());
        commands.put("help", new Help(commands));
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if(!(cs instanceof Player)){
            cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cOnly Players can use these commands"));
        }
        if(cmd.getName().equalsIgnoreCase("voidspawn")){
            if (args == null || args.length < 1) {
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Version &6" + plugin.getDescription().getVersion() + "&f by &6EnderCrest"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
                return true;
            }
            String sub = args[0].toLowerCase();
            if (!commands.containsKey(sub)) {
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat command does not exist"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
                return true;
            }
            try {
                commands.get(sub).onCommand((Player)cs, args);
            } catch (Exception e) {
                e.printStackTrace();
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThere was an error"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
            }
        }
        return false;
    }
}
