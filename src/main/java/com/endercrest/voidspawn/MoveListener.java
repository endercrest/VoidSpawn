package com.endercrest.voidspawn;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MoveListener implements Listener {

    private VoidSpawn plugin;

    public MoveListener(VoidSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR){
            TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());
        }

        if(player.getLocation().getY() <= 0){
            if(!player.hasPermission("vs.override")) {
                if(ConfigManager.getInstance().isModeSet(worldName)) {
                    if (ConfigManager.getInstance().getMode(worldName).equalsIgnoreCase("Spawn")) {
                        if (ConfigManager.getInstance().isWorldSpawnSet(worldName)) {
                            TeleportManager.getInstance().teleportSpawn(player);
                        } else {
                            player.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cContact Admin. Mode has been set but spawn has not been."));
                        }
                    } else if (ConfigManager.getInstance().getMode(worldName).equalsIgnoreCase("Touch")) {
                        TeleportManager.getInstance().teleportTouch(player);
                    }else if(ConfigManager.getInstance().getMode(worldName).equalsIgnoreCase("Island")){
                        TeleportManager.getInstance().teleportIsland(player);
                    }
                }
            }
        }
    }
}
