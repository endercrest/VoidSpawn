package com.endercrest.voidspawn.commands;

import org.bukkit.entity.Player;

import com.endercrest.voidspawn.VoidSpawn;

//TODO Revisit and clean up
public class PlayerToggle implements SubCommand {
    private VoidSpawn plugin;

    public PlayerToggle(VoidSpawn instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(Player p, String[] args) {
        if(p.hasPermission("vs.player.toggle")) {
            if(this.plugin.Toggle.containsKey(p.getUniqueId())) {
                if(!plugin.Toggle.get(p.getUniqueId())) {
                    plugin.Toggle.put(p.getUniqueId(), true);
                    p.sendMessage("Toggled teleport to: On");
                } else {
                    p.sendMessage("Toggled teleport to: Off");
                    plugin.Toggle.put(p.getUniqueId(), false);
                }
            } else {
                p.sendMessage("Toggled teleport to: Off");
                plugin.Toggle.put(p.getUniqueId(), false);
            }
            return true;
        } else {
            p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
            return false;
        }
    }

    @Override
    public String helpInfo() {
        return "/vs toggle - Turns off void teleport.";
    }

    @Override
    public String permission() {
        return "vs.player.toggle";
    }
}
