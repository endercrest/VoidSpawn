package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.entity.Player;
import pl.islandworld.IslandWorld;

public class Mode implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            return true;
        }
        if(args.length == 1){
            String modes = "&6Spawn&f, &6Touch&f, &6None&f";
            if(VoidSpawn.IslandWorld){
                modes = modes + ", &6Island&f";
            }
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Mode Types: " + modes));
        }else if(args.length == 2){
            if(VoidSpawn.isValidMode(args[1])) {
                if(args[1].equalsIgnoreCase("Island")){
                    if(p.getWorld().getName().equalsIgnoreCase(IslandWorld.getInstance().getIslandWorld().getName())){
                        ConfigManager.getInstance().setMode(p.getWorld().getName(), args[1]);
                        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Set mode for '&6" + p.getWorld().getName() + "&f'"));
                        return true;
                    }else{
                        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThis mode can not be set for this world"));
                        return true;
                    }
                }
                ConfigManager.getInstance().setMode(p.getWorld().getName(), args[1]);
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Set mode for '&6" + p.getWorld().getName() + "&f'"));
                if(!ConfigManager.getInstance().isWorldSpawnSet(p.getWorld().getName()) && args[1].equalsIgnoreCase("Spawn")){
                    p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Next set the &6spawn point."));
                }
                return true;
            }else{
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat is not a valid mode!"));
            }
        }else if(args.length >= 3){
            if(!VoidSpawn.isValidWorld(args[2])){
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                return true;
            }else{
                if(args[1].equalsIgnoreCase("Island")){
                    if(args[2].equalsIgnoreCase(IslandWorld.getInstance().getIslandWorld().getName())){
                        ConfigManager.getInstance().setMode(args[2], args[1]);
                        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Set mode for '&6" + args[2] + "&f'"));
                        return true;
                    }else{
                        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThis mode can not be set for this world"));
                        return true;
                    }
                }
                ConfigManager.getInstance().setMode(args[2], args[1]);
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Set mode for '&6" + args[2] + "&f'"));
            }
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/vs mode (mode) [world] - Sets world mode";
    }

    @Override
    public String permission() {
        return "vs.admin.mode";
    }
}
