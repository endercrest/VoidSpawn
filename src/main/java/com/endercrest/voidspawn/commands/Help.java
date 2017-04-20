package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.VoidSpawn;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Help implements SubCommand {

    HashMap<String, SubCommand> commands;

    public Help(HashMap<String, SubCommand> commands){
        this.commands = commands;
    }

    @Override
    public boolean onCommand(Player p, String[] args){
        if(!p.hasPermission(permission())){
            return true;
        }
        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "--- &6Help Menu&f ---"));
        for(String command : commands.keySet()){
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + commands.get(command).helpInfo()));
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
}