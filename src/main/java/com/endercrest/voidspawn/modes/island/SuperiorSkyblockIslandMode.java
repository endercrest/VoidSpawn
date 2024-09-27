package com.endercrest.voidspawn.modes.island;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.modes.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Stream;

public class SuperiorSkyblockIslandMode extends BaseIslandMode {
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
            for (World.Environment env : World.Environment.values()) {
                location = island.getTeleportLocation(env);
                if (location != null) break;
            }
        }

        if (location != null) {
            player.teleport(location);
            return TeleportResult.SUCCESS;
        }

        return TeleportResult.MISSING_ISLAND;
    }

    @Override
    public Status[] getStatus(String worldName) {
        boolean isSuperiorConfiguredCorrect = !SuperiorSkyblockAPI.getSettings().getVoidTeleport().isMembers() && !SuperiorSkyblockAPI.getSettings().getVoidTeleport().isVisitors();

        return Stream.concat(
                Arrays.stream(super.getStatus(worldName)),
                Stream.of(new Status(isSuperiorConfiguredCorrect ? Status.Type.COMPLETE : Status.Type.INCOMPLETE, "SuperiorSkyblock's 'void-teleport' disabled"))
        ).toArray(Status[]::new);
    }

    public static boolean isModeEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2");
    }
}
