package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.modes.Spawn;
import org.bukkit.entity.Player;

public class Set implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(!p.hasPermission(permission())){
            p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
            return true;
        }
        if(args.length > 1){
            String worldName = "";
            for(int i = 1; i < args.length; i++){
                worldName += args[i] + " ";
            }
            worldName = worldName.trim();
            if(!VoidSpawn.isValidWorld(worldName)){
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                return false;
            }
            ConfigManager.getInstance().setSpawn(p, worldName);
            ModeManager.getInstance().getSubMode("spawn").onSet(new String[]{}, worldName, p);
        }else{
            ConfigManager.getInstance().setSpawn(p, p.getWorld().getName());
            ModeManager.getInstance().getSubMode("spawn").onSet(new String[]{}, p.getWorld().getName(), p);
        }
        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Spawn Set & Mode set to Spawn"));
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs set [name] - Sets the spawn for the world";
    }

    @Override
    public String permission(){
        return "vs.admin.set";
    }
}