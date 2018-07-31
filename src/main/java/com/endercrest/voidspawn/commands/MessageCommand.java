package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(!p.hasPermission(permission())){
            p.sendMessage(MessageUtil.colorize("&cYou do not have permission."));
            return true;
        }
        if(args.length == 1){
            ConfigManager.getInstance().removeMessage(p.getWorld().getName());
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Message has been cleared."));
        }else if(args.length > 1){
            String message = "";
            for(int i = 1; i < args.length; i++){
                message += args[i] + " ";
            }
            ConfigManager.getInstance().setMessage(message, p.getWorld().getName());
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Message Set!"));
        }
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs message [message] - Adds a teleport message upon a player teleports, removes message if empty.";
    }

    @Override
    public String permission(){
        return "vs.admin.message";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        return new ArrayList<>();
    }
}