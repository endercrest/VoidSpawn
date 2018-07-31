package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.NumberUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

public class Offset implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(!p.hasPermission(permission())){
            p.sendMessage(MessageUtil.colorize("&cYou do not have permission."));
            return true;
        }
        if(args.length == 1){
            p.sendMessage(MessageUtil.colorize("&cAn offset is required."));
            return true;
        }else if(args.length > 1){
            if(NumberUtil.isInteger(args[1])){
                if(args.length > 2){
                    String worldName = "";
                    for(int i = 2; i < args.length; i++){
                        worldName += args[i] + " ";
                    }
                    worldName = worldName.trim();
                    if(!WorldUtil.isValidWorld(worldName)){
                        p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                        return false;
                    }
                    ConfigManager.getInstance().setOffset(Integer.parseInt(args[1]), worldName);
                }else{
                    ConfigManager.getInstance().setOffset(Integer.parseInt(args[1]), p.getWorld().getName());
                }
            }else{
                p.sendMessage(MessageUtil.colorize("&cOffset must be a number."));
                return true;
            }
        }
        p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Offset Set"));
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs offset (offset) [world] - Adds a teleport offset in the void.";
    }

    @Override
    public String permission(){
        return "vs.admin.offset";
    }
}