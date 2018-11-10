package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class Island implements SubMode {

    @Override
    public boolean onActivate(Player player, String worldName){
        return TeleportManager.getInstance().teleportIsland(player, worldName);
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p){
        ConfigManager.getInstance().setMode(worldName, args[1]);

        return true;
    }

    @Override
    public Status[] getStatus(String worldName){
        final boolean isSpawnSet = ConfigManager.getInstance().isWorldSpawnSet(worldName);
        final DecimalFormat df = new DecimalFormat("0.0#");
        final String worldSpawn = ConfigManager.getInstance().getString(worldName + ".spawn.world", worldName);
        final double x = ConfigManager.getInstance().getDouble(worldName + ".spawn.x", 0);
        final double y = ConfigManager.getInstance().getDouble(worldName + ".spawn.y", 0);
        final double z = ConfigManager.getInstance().getDouble(worldName + ".spawn.z", 0);

        final String coords = String.format("x: %s, y: %s, z: %s", df.format(x), df.format(y), df.format(z));
        final String location = worldSpawn.equalsIgnoreCase(worldName) ? coords : String.format("world: %s, %s", worldSpawn, coords);

        return new Status[]{
            new Status(isEnabled() ? StatusType.COMPLETE : StatusType.INCOMPLETE, "Mode Enabled"),
            new Status(
                isSpawnSet ? StatusType.COMPLETE : StatusType.INCOMPLETE,
                String.format(
                    "Fallback Spawn Set (%s)",
                    isSpawnSet ? location : "/vs set")
                ),
        };
    }

    @Override
    public boolean isEnabled(){
        return VoidSpawn.IslandWorld || VoidSpawn.ASkyBlock || VoidSpawn.BentoBox || VoidSpawn.USkyBlock;
    }

    @Override
    public String getHelp(){
        return "&6Island &f- Will teleport player back to IslandWorld island";
    }

    @Override
    public String getName(){
        return "Island";
    }
}