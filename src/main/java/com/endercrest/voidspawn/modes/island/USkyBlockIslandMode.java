package com.endercrest.voidspawn.modes.island;

import com.endercrest.voidspawn.TeleportResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class USkyBlockIslandMode extends BaseIslandMode {

    @Override
    public TeleportResult onActivateIsland(Player player, String worldname) {
        return null;
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
        return Bukkit.getPluginManager().isPluginEnabled("uSkyBlock");
    }
}
