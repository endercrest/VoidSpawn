package com.endercrest.voidspawn.modes.island;

import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.modes.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
//import us.talabrek.ultimateskyblock.api.IslandInfo;
//import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;


// uSkyblock Maven is offline: https://github.com/uskyblock/uSkyBlock/issues/57
// Currently dropping support for it in this plugin

public class USkyBlockIslandMode extends BaseIslandMode {
//    private final uSkyBlockAPI usb;

    public USkyBlockIslandMode() {
//        this.usb = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");
    }

    @Override
    public TeleportResult onActivateIsland(Player player, String worldname) {
//        IslandInfo info = usb.getIslandInfo(player);
//        if (info.getWarpLocation() != null) {
//            player.teleport(info.getWarpLocation());
//            return TeleportResult.SUCCESS;
//        }
//        if (info.getIslandLocation() != null) {
//            player.teleport(info.getIslandLocation());
//            return TeleportResult.SUCCESS;
//        }
//
//        return TeleportResult.MISSING_ISLAND;
        return TeleportResult.MODE_DISABLED;
    }

    @Override
    public Status[] getStatus(String worldName) {
        return new Status[0];
    }

    @Override
    public boolean isEnabled() {
//        return isModeEnabled();
        return false;
    }

    public static boolean isModeEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("uSkyBlock");
    }
}
