package com.endercrest.voidspawn.modes.island;

import com.endercrest.voidspawn.TeleportResult;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ASkyblockIslandMode extends BaseIslandMode {
    @Override
    public TeleportResult onActivateIsland(Player player, String worldname) {
        if (ASkyBlockAPI.getInstance().hasIsland(player.getUniqueId()) || ASkyBlockAPI.getInstance().inTeam(player.getUniqueId())) {
            Location location = ASkyBlockAPI.getInstance().getHomeLocation(player.getUniqueId());
            if (location != null) {
                player.teleport(location);
                return TeleportResult.SUCCESS;
            }
            Location loc = ASkyBlockAPI.getInstance().getIslandLocation(player.getUniqueId());
            if (loc != null && loc.getWorld() != null) {
                loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
                player.teleport(loc);
            } else {
                player.teleport(ASkyBlockAPI.getInstance().getSpawnLocation());
            }
            return TeleportResult.SUCCESS;
        }

        player.teleport(ASkyBlockAPI.getInstance().getSpawnLocation());
        return TeleportResult.SUCCESS;
    }

    @Override
    public boolean isEnabled() {
        return isModeEnabled();
    }

    public static boolean isModeEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("ASkyBlock");
    }
}
