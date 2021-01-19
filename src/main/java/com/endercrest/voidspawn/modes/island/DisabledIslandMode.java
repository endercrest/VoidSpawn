package com.endercrest.voidspawn.modes.island;

import com.endercrest.voidspawn.TeleportResult;
import org.bukkit.entity.Player;

public class DisabledIslandMode extends BaseIslandMode {
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public TeleportResult onActivateIsland(Player player, String worldname) {
        return TeleportResult.MODE_DISABLED;
    }
}
