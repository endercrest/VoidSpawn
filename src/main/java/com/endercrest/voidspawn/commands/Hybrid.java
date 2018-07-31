package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

public class Hybrid implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(!p.hasPermission(permission())){
            p.sendMessage(MessageUtil.colorize("&cYou do not have permission."));
            return true;
        }

        if(args.length == 1){
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cMust include true or false!"));
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&c" + helpInfo()));
            return false;
        }

        if(!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")){
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cMust be true or false!"));
            return false;
        }

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
            ConfigManager.getInstance().setHybrid(Boolean.parseBoolean(args[1]), worldName);
        }else{
            ConfigManager.getInstance().setHybrid(Boolean.parseBoolean(args[1]), p.getWorld().getName());
        }
        p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Updated hybrid mode!"));
        return false;
    }

    @Override
    public String helpInfo(){
        return "/vs hybrid (true/false) [name] - Sets hybrid mode.";
    }

    @Override
    public String permission(){
        return "vs.admin.hybrid";
    }
}