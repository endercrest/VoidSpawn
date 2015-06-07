package com.endercrest.voidspawn;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private VoidSpawn plugin;

    public MoveListener(VoidSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid())
            TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());

        if(player.getLocation().getY() <= 0)
            if(!player.hasPermission("vs.override"))
                if(ConfigManager.getInstance().isModeSet(worldName))
                    for(String mode: ModeManager.getInstance().getModes().keySet())
                        if(ConfigManager.getInstance().getMode(worldName).equalsIgnoreCase(mode))
                            ModeManager.getInstance().getSubMode(mode).onActivate(player, worldName);
    }
}
