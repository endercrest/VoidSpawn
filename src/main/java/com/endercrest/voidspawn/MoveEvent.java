package com.endercrest.voidspawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * The Listener for player movement
 */
public class MoveEvent implements Listener {

    VoidSpawn plugin;

    public MoveEvent(VoidSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        String worldN = p.getWorld().getName();

        if (isSet(worldN)) {
            if(!p.hasPermission("vs.override")) {
                if (p.getLocation().getY() <= 0) {
                    double x = plugin.getConfig().getDouble(worldN + ".x");
                    double y = plugin.getConfig().getDouble(worldN + ".y");
                    double z = plugin.getConfig().getDouble(worldN + ".z");
                    float pitch = (float) plugin.getConfig().getDouble(worldN + ".pitch");
                    float yaw = (float) plugin.getConfig().getDouble(worldN + ".yaw");
                    World world = plugin.getServer().getWorld(plugin.getConfig().getString(worldN + ".world"));

                    Location loc = new Location(world, x, y, z, yaw, pitch);
                    p.setFallDistance(0F);
                    p.teleport(loc);
                }
            }
        }
    }

    public boolean isSet(String world){
        if(plugin.getConfig().isSet(world)){
            if(plugin.getConfig().isSet(world + ".x")){
                if(plugin.getConfig().isSet(world + ".y")){
                    if(plugin.getConfig().isSet(world + ".z")){
                        if(plugin.getConfig().isSet(world + ".pitch")){
                            if(plugin.getConfig().isSet(world + ".yaw")){
                                if(plugin.getConfig().isSet(world + ".world")){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
