package com.endercrest.voidspawn;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TouchTracker implements Runnable {
    private final boolean compatibilityMode;

    public TouchTracker(boolean compatibilityMode) {
        this.compatibilityMode = compatibilityMode;
    }

    @Override
    public void run() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && !isConflictingBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType())){
                TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());
            }
        }
    }

    public boolean isConflictingBlock(Material mat) {
        // This is a small hack to allow for pre-1.13 servers to run this version.
        // It should be a temporary measure and at some point VoidSpawn should just
        // disable itself if running on an unsupported version. But that will come
        // another day.
        if (compatibilityMode) {
            return false;
        }

        return Tag.TRAPDOORS.isTagged(mat);
    }
}
