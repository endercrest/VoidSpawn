package com.endercrest.voidspawn.modes.island;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.endercrest.voidspawn.TeleportResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SuperiorSkyblockIslandMode extends BaseIslandMode{
    @Override
    public boolean isEnabled() {

        return isModeEnabled();
    }

    @Override
    public TeleportResult onActivateIsland(Player player, String worldname) {
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player);

        Island island = superiorPlayer.getIsland();
        if (island == null)
            return TeleportResult.MISSING_ISLAND;

        Location location = island.getTeleportLocation(World.Environment.NORMAL);
        if (location == null) {
            for (World.Environment env: World.Environment.values()) {
                location = island.getTeleportLocation(env);
                if (location != null) break;
            }
        }

        if (location != null) {
            return TeleportResult.SUCCESS;
        }

        return TeleportResult.MISSING_ISLAND;
    }

    public static boolean isModeEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2");
    }
}
