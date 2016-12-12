package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.NumberUtil;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 12/10/2016.
 */
public class Offset implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
            return true;
        }
        if(args.length == 1){
            p.sendMessage(VoidSpawn.colorize("&cAn offset is required."));
            return true;
        }else if(args.length > 1){
            if(NumberUtil.isInteger(args[1])) {
                if (args.length > 2) {
                    String worldName = "";
                    for (int i = 2; i < args.length; i++) {
                        worldName += args[i] + " ";
                    }
                    worldName = worldName.trim();
                    if(!VoidSpawn.isValidWorld(worldName)) {
                        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                        return false;
                    }
                    ConfigManager.getInstance().setOffset(Integer.parseInt(args[1]), worldName);
                } else {
                    ConfigManager.getInstance().setOffset(Integer.parseInt(args[1]), p.getWorld().getName());
                }
            }else{
                p.sendMessage(VoidSpawn.colorize("&cOffset must be a number."));
                return true;
            }
        }
        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Offset Set"));
        return true;
    }

    @Override
    public String helpInfo() {
        return "/vs offset (offset) [world] - Adds a teleport offset in the void.";
    }

    @Override
    public String permission() {
        return "vs.admin.offset";
    }
}
