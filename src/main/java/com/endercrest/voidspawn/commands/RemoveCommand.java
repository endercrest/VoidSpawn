package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(!p.hasPermission(permission())){
            p.sendMessage(MessageUtil.colorize("&cYou do not have permission."));
            return true;
        }
        if(args.length > 1){
            String worldName = "";
            for(int i = 1; i < args.length; i++){
                worldName += args[i] + " ";
            }

            if(!WorldUtil.isValidWorld(worldName)){
                p.sendMessage(MessageUtil.colorize("&cThat world does not exist!"));
                return true;
            }
            ConfigManager.getInstance().removeSpawn(worldName);
        }else{
            ConfigManager.getInstance().removeSpawn(p);
        }
        p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&6Spawn Removed"));
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs remove [name] - Removes the spawn for the world";
    }

    @Override
    public String permission(){
        return "vs.admin.remove";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        switch(args.length) {
            case 1:
                return WorldUtil.getMatchingWorlds(args[0]);
            default:
                return new ArrayList<>();
        }
    }
}