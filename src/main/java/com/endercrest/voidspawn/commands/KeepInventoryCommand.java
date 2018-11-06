package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.CommandUtil;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class KeepInventoryCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(args.length == 1){
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cMust include true or false!"));
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&c" + helpInfo()));
            return false;
        }

        if(!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")){
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cMust be true or false!"));
            return false;
        }

        String world = CommandUtil.constructWorldFromArgs(args, 2, p.getWorld().getName());
        if(world == null) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
            return false;
        }

        ConfigManager.getInstance().setKeepInventory(Boolean.parseBoolean(args[1]), world);
        p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Updated keep inventory flag!"));
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs keepinventory (true/false) [name] - Sets the keep inventory flag.";
    }

    @Override
    public String permission(){
        return "vs.admin.keepinventory";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        return VoidSpawnTabCompleter.getBooleanWorldCompletion(args);
    }
}