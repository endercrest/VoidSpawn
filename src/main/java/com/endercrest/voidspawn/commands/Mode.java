package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.modes.SubMode;
import org.bukkit.entity.Player;

public class Mode implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())){
            return true;
        }
        if(args.length == 1){
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "--- &6Available Modes&f ---"));
            for(String s: ModeManager.getInstance().getModes().keySet()){
                SubMode mode = ModeManager.getInstance().getSubMode(s);
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + mode.getHelp()));
            }
        }else if(args.length == 2) {
            if (ModeManager.getInstance().getModes().containsKey(args[1].toLowerCase())) {
            SubMode mode = ModeManager.getInstance().getSubMode(args[1].toLowerCase());
                if (mode.onSet(args, p.getWorld().getName(), p)) {
                    p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Set mode for '&6" + p.getWorld().getName() + "&f'"));
                    return true;
                }
            }else {
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThis is not a valid mode!"));
            }
        }else if(args.length >= 3) {
            if(!VoidSpawn.isValidWorld(args[2])) {
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                return false;
            }
            if(ModeManager.getInstance().getModes().containsKey(args[1].toLowerCase())) {
                SubMode mode = ModeManager.getInstance().getSubMode(args[1].toLowerCase());
                if(mode.onSet(args, args[2], p)) {
                    p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Set mode for '&6" + p.getWorld().getName() + "&f'"));
                    return true;
                }
            }else {
                p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThis is not a valid mode!"));
            }
        }
        return false;
    }

    @Override
    public String helpInfo() {
        return "/vs mode (mode) [world] - Sets world mode";
    }

    @Override
    public String permission() {
        return "vs.admin.mode";
    }
}
