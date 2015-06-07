package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.entity.Player;

public class Set implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
            return true;
        }
        if(args.length > 1){
            ConfigManager.getInstance().setSpawn(args[1], p);
        }else{
            ConfigManager.getInstance().setSpawn(p);
        }
        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Spawn Set"));
        return true;
    }

    @Override
    public String helpInfo() {
        return "/vs set [name] - Sets the spawn for the world";
    }

    @Override
    public String permission() {
        return "vs.admin.set";
    }
}
