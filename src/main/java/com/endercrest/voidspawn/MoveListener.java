package com.endercrest.voidspawn;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handles updating player location for touch mode.
 */
public class MoveListener implements Listener {

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && !isTrapDoor(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType())){
            TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());
        }
    }
    
    public boolean isTrapDoor(Material mat) {
        return (mat == Material.OAK_TRAPDOOR || mat == Material.SPRUCE_TRAPDOOR || mat == Material.BIRCH_TRAPDOOR || mat == Material.JUNGLE_TRAPDOOR || mat == Material.ACACIA_TRAPDOOR || mat == Material.DARK_OAK_TRAPDOOR || mat == Material.IRON_TRAPDOOR);
    }    
    
}
