package com.endercrest.voidspawn.modes.island;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportManager;
import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.status.Status;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public abstract class BaseIslandMode extends BaseMode {

    public abstract TeleportResult onActivateIsland(Player player, String worldname);

    @Override
    public final TeleportResult onActivate(Player player, String worldName) {
        TeleportResult result = onActivateIsland(player, worldName);
        if (result != TeleportResult.SUCCESS) {
            if (ConfigManager.getInstance().isWorldSpawnSet(worldName)) {
                return TeleportManager.getInstance().teleportSpawn(player, worldName);
            }
        }

        return result;
    }

    @Override
    public Status[] getStatus(String worldName) {
        Location location = ConfigManager.getInstance().getSpawn(worldName);

        final DecimalFormat df = new DecimalFormat("0.0#");

        String msg = "/vs set";
        if (location != null) {
            msg = String.format("x: %s, y: %s, z: %s", df.format(location.getX()), df.format(location.getY()), df.format(location.getZ()));
            if (location.getWorld() != null && !location.getWorld().getName().equalsIgnoreCase(worldName)) {
                msg = String.format("world: %s, %s", location.getWorld().getName(), msg);
            }
        }

        return new Status[]{
                new Status(isEnabled() ? Status.Type.COMPLETE : Status.Type.INCOMPLETE, "Mode Enabled"),
                new Status(
                        location != null ? Status.Type.COMPLETE : Status.Type.INCOMPLETE,
                        String.format("Fallback Spawn Set (%s)", msg)
                ),
        };
    }

    @Override
    public String getHelp() {
        return "&6Island &f- Will teleport player back to their island";
    }

    @Override
    public String getName() {
        return "Island";
    }
}
