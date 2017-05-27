package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Command implements SubMode {

    @Override
    public boolean onActivate(Player player, String worldName){
        player.setFallDistance(0);
        String commandString = ConfigManager.getInstance().getString(worldName + ".command")
                .replace("${player.name}", player.getName())
                .replace("${player.uuid}", player.getUniqueId().toString())
                .replace("${player.coord.x}", player.getLocation().getBlockX()+"")
                .replace("${player.coord.y}", player.getLocation().getBlockY()+"")
                .replace("${player.coord.z}", player.getLocation().getBlockZ()+"")
                .replace("${player.coord.world}", player.getLocation().getWorld().getName());
        String[] commands = commandString.split(";");
        boolean success = true;
        for(String command : commands){
            boolean status;
            String[] perms = command.split(":", 2);
            //Check if cmd needs to be ran as OP/Console
            if(perms.length > 1 && perms[0].equalsIgnoreCase("op")){
                String cmd = perms[1];
                status = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.trim());
            }else{
                status = player.performCommand(command.trim());
            }

            if(!status)
                success = false;
        }
        if(!success){
            player.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cContact Admin. One of the commands failed."));
        }
        return success;
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p){
        ConfigManager.getInstance().setMode(worldName, args[1]);
        return true;
    }

    @Override
    public String getHelp(){
        return "&6Command &f- Uses configurable command(s) to send player to spawn";
    }
}