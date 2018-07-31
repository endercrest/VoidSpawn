package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(!p.hasPermission(permission())){
            p.sendMessage(MessageUtil.colorize("&cYou do not have permission."));
            return true;
        }
        ConfigManager.getInstance().reloadConfig();
        p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&6Plugin Reloaded"));
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs reload - Reloads VoidSpawn configs";
    }

    @Override
    public String permission(){
        return "vs.admin.reload";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        return new ArrayList<>();
    }
}