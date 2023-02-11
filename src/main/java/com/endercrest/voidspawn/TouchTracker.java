package com.endercrest.voidspawn;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public class TouchTracker implements Runnable {
    private static final double MAX_OFFSET = 0.0001;
    private static final double BLOCK_MID_OFFSET = 0.5;

    public TouchTracker() {
    }

    private boolean checkBlock(Player player, Block block) {
        if (block.getType().isSolid() && !isConflictingBlock(block.getType())) {
            Location blockLoc = block.getLocation();
            blockLoc.add(BLOCK_MID_OFFSET, 1, BLOCK_MID_OFFSET);
            TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), blockLoc);
            return true;
        }
        return false;
    }

    private Set<Block> getBlocksToCheck(Player player) {
        Set<Block> corners = new HashSet<>();
        World world = player.getWorld();
        int yBelow = player.getLocation().getBlockY() - 1;
        BoundingBox bb = player.getBoundingBox();
        double minX = bb.getMinX();
        double minZ = bb.getMinZ();
        double maxX = bb.getMaxX() - MAX_OFFSET;
        double maxZ = bb.getMaxZ() - MAX_OFFSET;
        // add min x, min z
        Location loc = new Location(world, minX, yBelow, minZ);
        corners.add(loc.getBlock());
        // min x, max z
        loc.setZ(maxZ);
        corners.add(loc.getBlock());
        // max x, max z
        loc.setX(maxX);
        corners.add(loc.getBlock());
        // max x, min z
        loc.setZ(minZ);
        corners.add(loc.getBlock());
        return corners;
    }

    private void tryAddingBlockFor(Player player) {
        for (Block block : getBlocksToCheck(player)) {
            if (checkBlock(player, block)) {
                return;
            }
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            tryAddingBlockFor(player);
        }
    }

    public boolean isConflictingBlock(Material mat) {
        return Tag.TRAPDOORS.isTagged(mat);
    }
}
