package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.entity.Player;

public class Reload implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(!p.hasPermission(permission())) {
            p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
            return true;
        }
        ConfigManager.getInstance().reloadConfig();
        p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&6Plugin Reloaded"));
        return true;
    }

    @Override
    public String helpInfo() {
        return "/vs reload - Reloads VoidSpawn configs";
    }

    @Override
    public String permission() {
        return "vs.admin.reload";
    }
}