package com.endercrest.voidspawn;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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

        if(!p.hasPermission("vs.override")) {
            if (p.getLocation().getY() <= 0) {
                if (isWorldSpawnSet(worldN)) {
                    teleportWorldSpawn(worldN, p);
                }else if(isWorldRandomSet(worldN)){
                    teleportRandom(p);
                }
            }
        }
    }

    public boolean isWorldSpawnSet(String world){
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

    public boolean isWorldRandomSet(String world){
        if(plugin.getConfig().isSet(world)){
            if(plugin.getConfig().isSet(world + ".random")){
                if(plugin.getConfig().getBoolean(world + ".random")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void teleportWorldSpawn(String worldN, Player p){
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

    public void teleportRandom(Player p){
        World world = p.getWorld();

        if(plugin.getConfig().isSet(world.getName() + ".world")){
            world = plugin.getServer().getWorld(plugin.getConfig().getString(world.getName() + ".world"));
        }

            double radius = plugin.getConfig().getDouble(world.getName() + ".radius");
            double xMax = radius + world.getSpawnLocation().getBlockX();
            double xMin = -radius + world.getSpawnLocation().getBlockX();
            double zMax = radius + world.getSpawnLocation().getBlockZ();
            double zMin = -radius + world.getSpawnLocation().getBlockZ();

            double randomX = Math.random();
            double randomZ = Math.random();

            double newX = xMin + (int)(randomX * (xMax - xMin)) + 0.5;
            double newZ = zMin + (int)(randomZ * (zMax - zMin)) + 0.5;
            double newY = world.getHighestBlockYAt((int)newX, (int)newZ) + 1;

            if(world.getBlockAt((int)newX, (int)newY, (int)newZ) == null) {
                world.getBlockAt((int) newX, (int) newY + 1, (int) newZ).setType(Material.GLASS);
            }

            Location loc = new Location(world, newX, newY, newZ);

            p.setFallDistance(0F);
            p.teleport(loc);
    }

    public boolean isSafe(Location loc){
    World world = loc.getWorld();
    double y = loc.getY();
    //Check for suffocation
    loc.setY(y+1);
    Block block1 = world.getBlockAt(loc);
    loc.setY(y+2);
    Block block2 = world.getBlockAt(loc);
    if(!(block1.getTypeId()==0||block2.getTypeId()==0)){
        return false;//not safe, suffocated
    }
    //Check for lava/void
    for(double i=128;i>-1;i--){
        loc.setY(i);
        Block block = world.getBlockAt(loc);
        if(block.getTypeId()!=0){
            if(block.getType()==Material.LAVA){
                return false;//not safe, lava above or below you
            }else if(!(block.getType()==Material.TORCH||block.getType()==Material.REDSTONE_TORCH_ON||block.getType()==Material.REDSTONE_TORCH_OFF||block.getType()==Material.PAINTING)){
                    if(i<y){
                        loc.setY(-1);//set y to negitive 1 to end loop, we hit solid ground.
                    }
                    //Check for painful fall
                    if((y-i)>10){
                        return false;//would fall down at least 11 blocks = painful landing...
                    }
                }
            }else{
                if(i==0){
                    if(block.getTypeId()==0){
                        return false;//not safe, void
                    }
                }
            }
        }
        return true;
    }
}
