package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class SpawnMode implements IMode {

    @Override
    public boolean onActivate(Player player, String worldName) {
        if (ConfigManager.getInstance().isWorldSpawnSet(worldName)) {
            return TeleportManager.getInstance().teleportSpawn(player, worldName);
        }
        player.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cContact Admin. Mode has been set but spawn has not been."));
        return false;
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p) {
        ConfigManager.getInstance().setMode(worldName, "spawn");
        if (!ConfigManager.getInstance().isWorldSpawnSet(worldName)) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Next set the &6spawn point."));
        }
        return true;
    }

    @Override
    public Status[] getStatus(String worldName) {
        final boolean isSpawnSet = ConfigManager.getInstance().isWorldSpawnSet(worldName);
        final DecimalFormat df = new DecimalFormat("0.0#");
        final String worldSpawn = ConfigManager.getInstance().getString(worldName + ".spawn.world", worldName);
        final double x = ConfigManager.getInstance().getDouble(worldName + ".spawn.x", 0);
        final double y = ConfigManager.getInstance().getDouble(worldName + ".spawn.y", 0);
        final double z = ConfigManager.getInstance().getDouble(worldName + ".spawn.z", 0);

        final String coords = String.format("x: %s, y: %s, z: %s", df.format(x), df.format(y), df.format(z));
        final String location = worldSpawn.equalsIgnoreCase(worldName) ? coords : String.format("world: %s, %s", worldSpawn, coords);

        return new Status[]{
                new Status(
                        isSpawnSet ? StatusType.COMPLETE : StatusType.INCOMPLETE,
                        String.format(
                                "Spawn point set (%s)",
                                isSpawnSet ? location : "/vs set")
                ),
        };
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getHelp() {
        return "&6Spawn &f- Will teleport player to set spot.";
    }

    @Override
    public String getName() {
        return "Spawn";
    }
}