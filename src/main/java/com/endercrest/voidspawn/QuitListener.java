package com.endercrest.voidspawn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * QuitListener to remove player from memory to free resources.
 */
public class QuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        TeleportManager.getInstance().removePlayer(event.getPlayer().getUniqueId());
    }
}
