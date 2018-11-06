package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.CommandUtil;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        String world = CommandUtil.constructWorldFromArgs(args, 1, p.getWorld().getName());
        if(world == null) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
            return false;
        }
        ConfigManager.getInstance().removeSpawn(world);
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