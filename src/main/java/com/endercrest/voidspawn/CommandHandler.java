package com.endercrest.voidspawn;

import com.endercrest.voidspawn.commands.Message;
import com.endercrest.voidspawn.commands.SubCommand;
import java.util.HashMap;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private VoidSpawn plugin;
    private HashMap<String, SubCommand> commands;

    public CommandHandler(VoidSpawn plugin) {
        this.plugin = plugin;
        this.commands = new HashMap<String, SubCommand>();
        loadCommands();
    }

    /**
     * Load the commands into the HashMap that makes it accessible to players.
     */
    private void loadCommands() {
        this.commands.put("set", new com.endercrest.voidspawn.commands.Set());
        this.commands.put("remove", new com.endercrest.voidspawn.commands.Remove());
        this.commands.put("reload", new com.endercrest.voidspawn.commands.Reload());
        this.commands.put("modes", new com.endercrest.voidspawn.commands.Modes());
        this.commands.put("mode", new com.endercrest.voidspawn.commands.Mode());
        this.commands.put("help", new com.endercrest.voidspawn.commands.Help(this.commands));
        this.commands.put("message", new Message());
        this.commands.put("offset", new com.endercrest.voidspawn.commands.Offset());
        this.commands.put("command", new com.endercrest.voidspawn.commands.Command());
        this.commands.put("keepinventory", new com.endercrest.voidspawn.commands.KeepInventory());
        this.commands.put("hybrid", new com.endercrest.voidspawn.commands.Hybrid());
        this.commands.put("toggle", new com.endercrest.voidspawn.commands.PlayerToggle(this.plugin));
    }

    @Override
    public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        if (!(cs instanceof Player)) {
            if ((args.length >= 1) && (args[0].equalsIgnoreCase("reload"))) {
                ConfigManager.getInstance().reloadConfig();
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&6Plugin Reloaded"));
                return true;
            }

            cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cOnly Players can use these commands"));
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("voidspawn")) {
            if ((args == null) || (args.length < 1)) {
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Version &6"
                        + this.plugin.getDescription().getVersion() + "&f by &6EnderCrest"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
                return true;
            }
            String sub = args[0].toLowerCase();
            if (!this.commands.containsKey(sub)) {
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat command does not exist"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
                return true;
            }
            try {
                this.commands.get(sub).onCommand((Player) cs, args);
            } catch (Exception e) {
                e.printStackTrace();
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThere was an error"));
                cs.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Type &6/vs help &ffor command information"));
            }
        }
        return false;
    }
}