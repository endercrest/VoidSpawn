package com.endercrest.voidspawn.modes.island;

import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.modes.status.Status;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;


public class IridiumSkyblockIslandMode extends BaseIslandMode {

    public IridiumSkyblockIslandMode() {
    }

    @Override
    public TeleportResult onActivateIsland(Player player, String worldname) {
        IridiumSkyblockAPI instance = IridiumSkyblockAPI.getInstance();
        if (instance == null) {
            return TeleportResult.MODE_DISABLED;
        }

        Optional<Island> islandOptional = instance.getUser(player).getIsland();
        if (islandOptional.isEmpty()) {
            return TeleportResult.MODE_DISABLED;
        }

        Location location = islandOptional.get().getHome();

        player.teleport(location);
        return TeleportResult.SUCCESS;
    }

    @Override
    public Status[] getStatus(String worldName) {
        return new Status[0];
    }

    @Override
    public boolean isEnabled() {
        return isModeEnabled();
    }

    public static boolean isModeEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("IridiumSkyblock");
    }
}
