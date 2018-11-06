package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.modes.SubMode;
import com.endercrest.voidspawn.utils.CommandUtil;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args){
        String world = CommandUtil.constructWorldFromArgs(args, 1, p.getWorld().getName());
        if(world == null) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
            return false;
        }

        SubMode mode = ModeManager.getInstance().getWorldSubMode(world);

        List<String> messages = new ArrayList<>();
        messages.add(String.format("--- &6%s Info &f---", world));
        messages.add("Status:");
        messages.add(format(toType(mode != null), String.format("Mode Set (%s)", mode != null ? mode.getName() : "/vs mode")
        ));

        if(mode != null) {
            for(SubMode.Status status: mode.getStatus(world)){
                messages.add(format(status.getType(), status.getMessage()));
            }

            messages.add("Configurations:");
            messages.add(format(toType(ConfigManager.getInstance().isHybrid(world)),"Hybrid Mode"));
            messages.add(format(toType(ConfigManager.getInstance().getKeepInventory(world)),"Keep Inventory"));
            messages.add(format(toType(ConfigManager.getInstance().getMessage(world).isEmpty()), "Message Set"));
            messages.add(format(toType(ConfigManager.getInstance().isSoundSet(world)),"Sound Set"));
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

    private SubMode.StatusType toType(boolean status) {
        return status ? SubMode.StatusType.COMPLETE : SubMode.StatusType.INCOMPLETE;
    }

    private String format(SubMode.StatusType type, String message) {
        return String.format("  %s %s", getStatusText(type), message);
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
