package com.endercrest.voidspawn.modes.island;

import com.endercrest.voidspawn.TeleportResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.islandworld.IslandWorld;
import pl.islandworld.api.IslandWorldApi;
import pl.islandworld.entity.SimpleIsland;

public class IslandWorldIslandMode extends BaseIslandMode{

    @Override
    public boolean isEnabled() {
        return isModeEnabled();
    }

    @Override
    public TeleportResult onActivateIsland(Player player, String worldname) {
        if (IslandWorldApi.haveIsland(player.getName()) || IslandWorldApi.isHelpingIsland(player.getName())) {
            SimpleIsland island = IslandWorld.getInstance().getPlayerIsland(player);
            if (island == null) {
                island = IslandWorld.getInstance().getHelpingIsland(player);
            }
            if (island != null) {
                Location loc = island.getLocation().toLocation();
                loc.setWorld(IslandWorldApi.getIslandWorld());
                player.teleport(loc);
                return TeleportResult.SUCCESS;
            }
        }
        return TeleportResult.MISSING_ISLAND;
    }

    public static boolean isModeEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("IslandWorld");
    }
}
