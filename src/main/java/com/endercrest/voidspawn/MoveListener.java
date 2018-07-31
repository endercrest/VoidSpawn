package com.endercrest.voidspawn;

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
        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()){
            TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());
        }
    }
}