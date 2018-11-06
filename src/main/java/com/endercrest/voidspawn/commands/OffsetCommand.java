package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.CommandUtil;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.NumberUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OffsetCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(args.length == 1){
            p.sendMessage(MessageUtil.colorize("&cAn offset is required."));
            return true;
        }else if(args.length > 1){
            if(!NumberUtil.isInteger(args[1])) {
                p.sendMessage(MessageUtil.colorize("&cOffset must be a number."));
                return false;
            }

            String world = CommandUtil.constructWorldFromArgs(args, 2, p.getWorld().getName());
            if(world == null) {
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                return false;
            }

            ConfigManager.getInstance().setOffset(Integer.parseInt(args[1]), world);
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

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        switch (args.length) {
            case 1:
                return new ArrayList<String>() {{
                    add("-10");
                    add("-5");
                    add("0");
                    add("5");
                    add("10");
                }};
            case 2:
                return WorldUtil.getMatchingWorlds(args[1]);
        }
        return null;
    }
}