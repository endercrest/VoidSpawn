package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.DetectorManager;
import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.detectors.SubDetector;
import com.endercrest.voidspawn.modes.SubMode;
import org.bukkit.entity.Player;

public class Detector implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(!p.hasPermission(permission())){
            p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
            return true;
        }
        if(args.length == 1){
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "--- &6Available Detectors&f ---"));
            for(SubDetector detector : DetectorManager.getInstance().getDetectors().values()){
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + detector.getInfo()));
            }
        }else if(args.length == 2){
            if(DetectorManager.getInstance().getDetectors().containsKey(args[1].toLowerCase())){
                SubDetector detector = DetectorManager.getInstance().getDetector(args[1].toLowerCase());
                ConfigManager.getInstance().setDetector(detector.getName().toLowerCase(), p.getWorld().getName());
            }else{
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThis is not a valid detector!"));
            }
        }else if(args.length >= 3){
            String worldName = "";
            for(int i = 2; i < args.length; i++){
                worldName += args[i] + " ";
            }
            worldName = worldName.trim();

            if(!VoidSpawn.isValidWorld(worldName)){
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                return false;
            }
            if(DetectorManager.getInstance().getDetectors().containsKey(args[1].toLowerCase())){
                SubDetector detector = DetectorManager.getInstance().getDetector(args[1].toLowerCase());
                ConfigManager.getInstance().setDetector(detector.getName().toLowerCase(), worldName);
            }else{
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThis is not a valid detector!"));
            }
        }
        return false;
    }

    @Override
    public String helpInfo(){
        return "/vs detector (detector) [world] - Sets world detector";
    }

    @Override
    public String permission(){
        return "vs.admin.detector";
    }
}