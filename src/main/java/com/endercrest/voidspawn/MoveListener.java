package com.endercrest.voidspawn;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid())
            TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());

        if(player.getLocation().getY() <= 0) {
            int offset = Math.max(0, ConfigManager.getInstance().getOffSet(player.getWorld().getName()));
            if (player.getLocation().getY() < -offset) {
                if (ConfigManager.getInstance().isModeSet(worldName)) {
                    for (String mode : ModeManager.getInstance().getModes().keySet()) {
                        if (ConfigManager.getInstance().getMode(worldName).equalsIgnoreCase(mode)) {
                            String string = ConfigManager.getInstance().getMessage(worldName);
                            if (!string.equals(""))
                                player.sendMessage(VoidSpawn.colorize(string));
                            if(!ConfigManager.getInstance().getKeepInventory(worldName))
                                player.getInventory().clear();
                            boolean success = ModeManager.getInstance().getSubMode(mode).onActivate(player, worldName);
                            if (!success) {
                                player.sendMessage(VoidSpawn.colorize("&cAn error occurred! Please notify an administrator."));
                            }
                        }
                    }
                }
            }
        }
    }
}
