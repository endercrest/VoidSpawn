package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.NumberUtil;
import org.bukkit.entity.Player;

public class Sound implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(args.length == 1){
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix +
                    "All sounds available are listed here: https://goo.gl/jGcL8B or use 'clear' to remove the sound."));
            return true;
        }

        if(args.length >= 2){
            String soundName = args[1].toUpperCase();

            if(soundName.equalsIgnoreCase("clear")){
                ConfigManager.getInstance().removeSound(p.getWorld().getName());
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + String.format("Cleared sound from %s.", p.getWorld().getName())));
                return true;
            }

            org.bukkit.Sound sound;
            try{
                sound = org.bukkit.Sound.valueOf(soundName.toUpperCase());
            }catch(IllegalArgumentException e){
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + String.format("&c%s is not a valid sound!", soundName)));
                return false;
            }

            if(args.length >= 3){
                String volume = args[2];

                if(!NumberUtil.isFloat(volume)){
                    p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + String.format("&c%s is not a valid number.", volume)));
                    return false;
                }

                if(args.length >= 4){
                    String pitch = args[3];

                    if(!NumberUtil.isFloat(pitch)){
                        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + String.format("&c%s is not a valid number.", pitch)));
                        return false;
                    }

                    ConfigManager.getInstance().setPitch(p.getWorld().getName(), Float.parseFloat(pitch));
                }
                ConfigManager.getInstance().setVolume(p.getWorld().getName(), Float.parseFloat(volume));
            }
            ConfigManager.getInstance().setSound(p.getWorld().getName(), sound.name());
        }

        if(args.length >= 2){
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + String.format("Sound successfully set for %s.", p.getWorld().getName())));
        }else if(args.length >= 3){
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + String.format("Sound, and volume successfully set for %s.", p.getWorld().getName())));
        }else if(args.length >= 4){
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + String.format("Sound, volume, and pitch successfully set for %s.", p.getWorld().getName())));
        }
        return true;
    }

    @Override
    public String helpInfo(){
        return "/vs sound (sound) [volume] [pitch] - Sets the sound when entering the void.";
    }

    @Override
    public String permission(){
        return "cc.admin.sound";
    }
}
