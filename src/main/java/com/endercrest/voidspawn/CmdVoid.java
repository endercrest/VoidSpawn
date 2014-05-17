package com.endercrest.voidspawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Commands for void spawn
 */
public class CmdVoid implements CommandExecutor {

    private VoidSpawn plugin;

    public CmdVoid(VoidSpawn plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("void")){
            if(args.length == 0){
                return showHelp(cs);
            }else if(args.length > 0){
                if(args[0].equalsIgnoreCase("set")){
                    if(args.length == 1){
                        return setVoidSpawn(cs);
                    }else{
                        return showInvalid(cs);
                    }
                }else if(args[0].equalsIgnoreCase("help")){
                    return showHelp(cs);
                }else if(args[0].equalsIgnoreCase("clear")){
                    return clearVoidSpawn(cs);
                }else if(args[0].equalsIgnoreCase("random")){
                    return setRandomVoidSpawn(cs, args);
                }
            }
        }
        return true;
    }

    private boolean showInvalid(CommandSender cs){
        if(isConsole(cs)){
            return true;
        }
        cs.sendMessage(VoidSpawn.colorize("&cInvalid Parameters. '/void set'"));
        cs.sendMessage(VoidSpawn.colorize("&c() - Mandatory, [] - Optional"));
        return true;
    }

    private boolean showHelp(CommandSender cs){
        if(isConsole(cs)){
            return true;
        }
        cs.sendMessage(VoidSpawn.colorize("&9--- &aVoidSpawn Commands &9---"));
        cs.sendMessage(VoidSpawn.colorize("&a'/void set'"));
        cs.sendMessage(VoidSpawn.colorize("&a'/void random [radius]'"));
        cs.sendMessage(VoidSpawn.colorize("&a'/void clear'"));
        cs.sendMessage(VoidSpawn.colorize("&a'/void help'"));
        cs.sendMessage(VoidSpawn.colorize("&a() - Mandatory, [] - Optional"));
        return true;
    }

    private boolean setVoidSpawn(CommandSender cs) {
        if(isConsole(cs)){
            return true;
        }
        Player p = (Player) cs;
        Location loc = p.getLocation();
        plugin.getConfig().set(p.getWorld().getName() + ".x", loc.getX());
        plugin.getConfig().set(p.getWorld().getName() + ".y", loc.getY());
        plugin.getConfig().set(p.getWorld().getName() + ".z", loc.getZ());
        plugin.getConfig().set(p.getWorld().getName() + ".world", loc.getWorld().getName());
        plugin.getConfig().set(p.getWorld().getName() + ".pitch", loc.getPitch());
        plugin.getConfig().set(p.getWorld().getName() + ".yaw", loc.getYaw());
        plugin.getConfig().set(p.getWorld().getName() + ".random", null);
        plugin.getConfig().set(p.getWorld().getName() + ".radius", null);
        cs.sendMessage(VoidSpawn.colorize("&aVoidSpawn has been set!"));
        plugin.saveConfig();
        return true;
    }

    private boolean clearVoidSpawn(CommandSender cs){
        if(isConsole(cs)){
            return true;
        }
        Player p = (Player) cs;
        plugin.getConfig().set(p.getWorld().getName(), null);
        plugin.getConfig().set(p.getWorld().getName() + ".x", null);
        plugin.getConfig().set(p.getWorld().getName() + ".y", null);
        plugin.getConfig().set(p.getWorld().getName() + ".z", null);
        plugin.getConfig().set(p.getWorld().getName() + ".world", null);
        plugin.getConfig().set(p.getWorld().getName() + ".pitch", null);
        plugin.getConfig().set(p.getWorld().getName() + ".yaw", null);
        plugin.getConfig().set(p.getWorld().getName() + ".random", null);
        plugin.getConfig().set(p.getWorld().getName() + ".radius", null);
        cs.sendMessage(VoidSpawn.colorize("&aVoidSpawn has been cleared and disabled!"));
        plugin.saveConfig();
        return true;
    }

    private boolean isConsole(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(VoidSpawn.colorize("&4This command is only available to players!"));
            return true;
        }
        return false;
    }

    private boolean setRandomVoidSpawn(CommandSender cs, String[] args){
        if(isConsole(cs)){
            return true;
        }
        Player p = (Player) cs;
            if(args.length == 2) {
                if(isNumber(cs, args[1])) {
                    Integer radius = Integer.parseInt(args[1]);
                    plugin.getConfig().set(p.getWorld().getName() + ".radius", radius);
                    plugin.getConfig().set(p.getWorld().getName() + ".random", true);
                    cs.sendMessage(VoidSpawn.colorize("&aRandom Spawns have been set in this world. With the radius of " + args[1]));
                }
            }else if(args.length == 3) {
                if(isNumber(cs, args[1])) {
                    if (!plugin.getServer().getWorld(args[2]).equals(null)) {
                        World spawnWorld = plugin.getServer().getWorld(args[2]);
                        plugin.getConfig().set(p.getWorld().getName() + ".world", spawnWorld.getName());
                    }

                    Integer radius = Integer.parseInt(args[1]);
                    plugin.getConfig().set(p.getWorld().getName() + ".radius", radius);
                    plugin.getConfig().set(p.getWorld().getName() + ".random", true);
                    cs.sendMessage(VoidSpawn.colorize("&aRandom Spawns have been set in this world. With the radius of " + args[1]));
                }
            }else{
                plugin.getConfig().set(p.getWorld().getName() + ".radius", 500);
                plugin.getConfig().set(p.getWorld().getName() + ".random", true);
                cs.sendMessage(VoidSpawn.colorize("&aRandom Spawns have been set in this world. With the radius of 500"));
            }
        plugin.getConfig().set(p.getWorld().getName() + ".x", null);
        plugin.getConfig().set(p.getWorld().getName() + ".y", null);
        plugin.getConfig().set(p.getWorld().getName() + ".z", null);
        plugin.getConfig().set(p.getWorld().getName() + ".pitch", null);
        plugin.getConfig().set(p.getWorld().getName() + ".yaw", null);
        plugin.saveConfig();
        return true;
    }

    private boolean isNumber(CommandSender cs, String args){
        if(!args.matches("[0-9]+")){
            cs.sendMessage(VoidSpawn.colorize("&cYou can only have numbers in the radius."));
            return false;
        }
        return true;
    }
}
