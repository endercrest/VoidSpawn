package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.entity.Player;

public class Remove implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
            return true;
        }
        if(args.length > 1){
            if(!VoidSpawn.isValidWorld(args[1])){
                p.sendMessage(VoidSpawn.colorize("&cThat world does not exist!"));
                return true;
            }
            ConfigManager.getInstance().removeSpawn(args[1]);
        }else{
            ConfigManager.getInstance().removeSpawn(p);
        }
        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&6Spawn Removed"));
        return true;
    }

    @Override
    public String helpInfo() {
        return "/vs remove [name] - Removes the spawn for the world";
    }

    @Override
    public String permission() {
        return "vs.admin.remove";
    }
}
