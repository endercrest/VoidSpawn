package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.TeleportManager;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.entity.Player;

import com.endercrest.voidspawn.VoidSpawn;

import java.util.ArrayList;
import java.util.List;

public class ToggleCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        boolean status = TeleportManager.getInstance().togglePlayer(p.getUniqueId());
        if(status){
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Toggled teleport to: Off"));
        }else{
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Toggled teleport to: On"));
        }
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs toggle - Turns off void teleport.";
    }

    @Override
    public String permission(){
        return "vs.player.toggle";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        return new ArrayList<>();
    }
}
