package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.entity.Player;
import pl.islandworld.IslandWorld;

public class Island implements SubMode {
    @Override
    public boolean onActivate(Player player, String worldName) {
        TeleportManager.getInstance().teleportIsland(player);
        return true;
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p) {
        if(worldName.equalsIgnoreCase(IslandWorld.getInstance().getIslandWorld().getName())){
            ConfigManager.getInstance().setMode(worldName, args[1]);
            return true;
        }else{
            p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cThis mode can not be set for this world"));
            return false;
        }
    }

    @Override
    public String getHelp() {
        return "&6Island &f- Will teleport player back to IslandWorld island";
    }
}
