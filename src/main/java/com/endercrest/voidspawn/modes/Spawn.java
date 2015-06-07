package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.entity.Player;

public class Spawn implements SubMode {

    @Override
    public boolean onActivate(Player player, String worldName) {
        if (ConfigManager.getInstance().isWorldSpawnSet(worldName)) {
            TeleportManager.getInstance().teleportSpawn(player);
            return true;
        } else {
            player.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cContact Admin. Mode has been set but spawn has not been."));
            return false;
        }
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p) {
        ConfigManager.getInstance().setMode(worldName, args[1]);
        if(!ConfigManager.getInstance().isWorldSpawnSet(worldName) && args[1].equalsIgnoreCase("Spawn")){
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Next set the &6spawn point."));
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "&6Spawn &f- Will teleport player to set spot.";
    }
}
