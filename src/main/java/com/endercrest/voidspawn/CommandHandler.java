package com.endercrest.voidspawn;

import com.endercrest.voidspawn.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private VoidSpawn plugin;
    private HashMap<String, SubCommand> commands;

    public CommandHandler(VoidSpawn plugin){
        this.plugin = plugin;
        commands = new HashMap<>();
        loadCommands();
    }

    /**
     * Load the commands into the HashMap that makes it accessible to players.
     */
    private void loadCommands(){
        commands.put("set", new SetCommand());
        commands.put("remove", new RemoveCommand());
        commands.put("reload", new ReloadCommand());
        commands.put("modes", new ModesCommand());
        commands.put("mode", new ModeCommand());
        commands.put("help", new HelpCommand(commands));
        commands.put("command", new CommandCommand());
        commands.put("toggle", new ToggleCommand());
        commands.put("detector", new DetectorCommand());
        commands.put("info", new InfoCommand());
        commands.put("flag", new FlagCommand());
    }

    public Set<Map.Entry<String, SubCommand>> getCommands() {
        return commands.entrySet();
    }

    public SubCommand getSubCommand(String cmd) {
        return commands.get(cmd);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args){
        if(!(cs instanceof Player)){
            if((args.length >= 1) && (args[0].equalsIgnoreCase("reload"))){
                ConfigManager.getInstance().reloadConfig();
                cs.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&6Plugin Reloaded"));
                return true;
            }

            cs.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cOnly Players can use these commands"));
            return false;
        }
        if(cmd.getName().equalsIgnoreCase("voidspawn")){
            if((args == null) || (args.length < 1)){
                cs.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Version &6" + plugin.getDescription().getVersion() + "&f by &6EnderCrest"));
                cs.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
                return true;
            }
            String sub = args[0].toLowerCase();
            if(!commands.containsKey(sub)){
                cs.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat command does not exist"));
                cs.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
                return true;
            }
            SubCommand command = commands.get(sub);
            Player player = (Player)cs;
            if(command.permission() != null && !player.hasPermission(command.permission())) {
                player.sendMessage(MessageUtil.colorize("&cYou do not have permission."));
                return true;
            }


            try {
                command.onCommand(player, args);
            } catch (Exception e) {
                e.printStackTrace();
                cs.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThere was an error"));
                cs.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
            }
        }
        return false;
    }
}