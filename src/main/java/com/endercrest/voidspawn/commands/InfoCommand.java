package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.modes.SubMode;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args){
        String world = p.getWorld().getName();
        if(args.length > 1) {
            String worldName = "";
            for(int i = 1; i < args.length; i++){
                worldName += args[i] + " ";
            }
            worldName = worldName.trim();
            if(!WorldUtil.isValidWorld(worldName)){
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                return false;
            }
            world = worldName;
        }
        SubMode mode = ModeManager.getInstance().getWorldSubMode(world);

        List<String> messages = new ArrayList<>();
        messages.add(String.format("--- &6%s Info &f---", world));
        messages.add("Status:");
        messages.add(String.format(
            "  %s %s",
            getStatusText(mode != null ? SubMode.StatusType.COMPLETE : SubMode.StatusType.INCOMPLETE),
            String.format("Mode Set (%s)", mode != null ? mode.getName() : "/vs mode")
        ));


        if(mode != null) {
            for(SubMode.Status status: mode.getStatus(world)){
                messages.add(String.format("  %s %s", getStatusText(status.getType()), status.getMessage()));
            }
        }
        for(String message: messages){
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + message));
        }
        return false;
    }

    private String getStatusText(SubMode.StatusType type) {
        if(type == SubMode.StatusType.COMPLETE) {
            return "[&a+&f]";
        } else if (type == SubMode.StatusType.INCOMPLETE) {
            return "[&c-&f]";
        } else{
            return "[&b!&f]";
        }
    }

    @Override
    public String helpInfo(){
        return "/vs info [name] - Get VoidSpawn info for the given world";
    }

    @Override
    public String permission(){
        return "vs.admin.status";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args){
        switch(args.length) {
            case 1:
                return WorldUtil.getMatchingWorlds(args[0]);
            default:
                return new ArrayList<>();
        }
    }
}
