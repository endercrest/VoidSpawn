package com.endercrest.voidspawn;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TouchTracker implements Runnable {
    @Override
    public void run() {
        for (Player player: Bukkit.getOnlinePlayers()) {
            if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && !isConflictingBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType())){
                TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());
            }
        }
    }

    public boolean isConflictingBlock(Material mat) {
        return Tag.TRAPDOORS.isTagged(mat);
    }
}
