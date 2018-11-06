package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.DetectorManager;
import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.detectors.SubDetector;
import com.endercrest.voidspawn.utils.CommandUtil;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DetectorCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args){
        if(args.length == 1){
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "--- &6Available Detectors&f ---"));
            for(SubDetector detector : DetectorManager.getInstance().getDetectors().values()){
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + detector.getInfo()));
            }
        }else if(args.length >= 2) {
            String world = CommandUtil.constructWorldFromArgs(args, 2, p.getWorld().getName());
            if(world == null) {
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                return false;
            }

            if(DetectorManager.getInstance().getDetectors().containsKey(args[1].toLowerCase())){
                SubDetector detector = DetectorManager.getInstance().getDetector(args[1].toLowerCase());
                ConfigManager.getInstance().setDetector(detector.getName().toLowerCase(), world);
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Updated detector!"));
            }else{
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThis is not a valid detector!"));
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

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        switch(args.length) {
            case 1:
                Set<String> detectors = DetectorManager.getInstance().getDetectors().keySet();
                return detectors.stream()
                        .filter(detector -> detector.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            case 2:
                return WorldUtil.getMatchingWorlds(args[1]);
            default:
                return new ArrayList<>();
        }
    }
}