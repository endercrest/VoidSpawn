package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.VoidSpawn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {

    HashMap<String, SubCommand> commands;

    public HelpCommand(HashMap<String, SubCommand> commands){
        this.commands = commands;
    }

    @Override
    public boolean onCommand(Player p, String[] args){
        p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "--- &6Help Menu&f ---"));
        for(String command : commands.keySet()){
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + commands.get(command).helpInfo()));
        }
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs help - Gets plugin help";
    }

    @Override
    public String permission(){
        return "vs.admin.help";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        return new ArrayList<>();
    }
}