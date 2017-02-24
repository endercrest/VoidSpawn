package com.endercrest.voidspawn;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pl.islandworld.IslandWorld;
import pl.islandworld.api.IslandWorldApi;
import pl.islandworld.entity.MyLocation;
import pl.islandworld.entity.SimpleIsland;
import us.talabrek.ultimateskyblock.api.IslandInfo;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class TeleportManager {

    private VoidSpawn plugin;
    private static TeleportManager instance = new TeleportManager();
    private HashMap<UUID, Location> playerLocation = new HashMap<UUID, Location>();

    public static TeleportManager getInstance(){
        return instance;
    }

    public void setUp(VoidSpawn plugin){
        this.plugin = plugin;
    }

    /**
     * Teleport the player to the selected world.
     * @param player The Player that will be teleported.
     * @param worldName Name of world to get coordinates from.
     * @return Whether the teleport was successful.
     */
    public boolean teleportSpawn(Player player, String worldName){
        double x = ConfigManager.getInstance().getDouble(worldName + ".spawn.x");
        double y = ConfigManager.getInstance().getDouble(worldName + ".spawn.y");
        double z = ConfigManager.getInstance().getDouble(worldName + ".spawn.z");
        float pitch = ConfigManager.getInstance().getFloat(worldName + ".spawn.pitch");
        float yaw = ConfigManager.getInstance().getFloat(worldName + ".spawn.yaw");
        World world = plugin.getServer().getWorld(ConfigManager.getInstance().getString(worldName + ".spawn.world"));

        Location location = new Location(world, x, y, z, yaw, pitch);
        player.setFallDistance(0);
        player.teleport(location);
        return true;
    }

    /**
     * Update the players location for touch spawn mode.
     * @param uuid The UUID of the player.
     * @param loc The location of the player.
     */
    public void setPlayerLocation(UUID uuid, Location loc){
        playerLocation.put(uuid, loc);
    }

    /**
     * Teleports the player to thier last touched location.
     * @param p The Player that will be teleported.
     * @return Whether the teleport was successful.
     */
    public boolean teleportTouch(Player p){
        UUID uuid = p.getUniqueId();
        Location loc;
        if(playerLocation.get(uuid) == null){
           loc = p.getLocation();
        }else {
            loc = playerLocation.get(uuid);
        }
        Location below = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
        if(below.getBlock().getType().equals(Material.AIR)){
            for(int i = 1; i < 10; i++){
                Location newLoc = new Location(loc.getWorld(), loc.getX() + i, loc.getWorld().getHighestBlockYAt(loc.getBlockX() + i, loc.getBlockZ()), loc.getZ());
                Location newLocBelow = new Location(loc.getWorld(), loc.getX() + i, loc.getWorld().getHighestBlockYAt(loc.getBlockX() + i, loc.getBlockZ()) - 1, loc.getZ());
                if(!newLocBelow.getBlock().getType().equals(Material.AIR)){
                    p.setFallDistance(0);
                    p.teleport(newLoc);
                    return true;
                }
            }
            for(int i = 1; i < 10; i++){
                Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ() + i), loc.getZ() + i);
                Location newLocBelow = new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) - 1, loc.getZ() + i);
                if(!newLocBelow.getBlock().getType().equals(Material.AIR)){
                    p.setFallDistance(0);
                    p.teleport(newLoc);
                    return true;
                }
            }
            p.setFallDistance(0);
            p.teleport(loc.getWorld().getSpawnLocation());
            return true;
        }
        p.setFallDistance(0);
        p.teleport(loc);
        return true;
    }

    /**
     * Teleports the player to their sky block island. (Only Available if IslandWorld or ASkyBlock is installed).
     * @param p The player that will be teleported.
     * @return Whether the teleport was successful.
     */
    public boolean teleportIsland(Player p){
        if(VoidSpawn.IslandWorld) {
            if (IslandWorldApi.haveIsland(p.getName()) || IslandWorldApi.isHelpingIsland(p.getName())) {
                SimpleIsland island = IslandWorld.getInstance().getPlayerIsland(p);
                if(island != null) {
                    p.setFallDistance(0);
                    Location loc = island.getLocation().toLocation();
                    loc.setWorld(IslandWorldApi.getIslandWorld());
                    p.teleport(loc);
                    return true;
                }else{
                    island = IslandWorld.getInstance().getHelpingIsland(p);
                    if(island != null){
                        p.setFallDistance(0);
                        Location loc = island.getLocation().toLocation();
                        loc.setWorld(IslandWorldApi.getIslandWorld());
                        p.teleport(loc);
                        return true;
                    }
                }
            }
        }else if(VoidSpawn.ASkyBlock){
            if(ASkyBlockAPI.getInstance().hasIsland(p.getUniqueId()) || ASkyBlockAPI.getInstance().inTeam(p.getUniqueId())){
                Location location = ASkyBlockAPI.getInstance().getHomeLocation(p.getUniqueId());
                if(location != null) {
                    p.setFallDistance(0);
                    p.teleport(location);
                    return true;
                }else{
                    Location loc = ASkyBlockAPI.getInstance().getIslandLocation(p.getUniqueId());
                    p.setFallDistance(0);
                    if(loc != null) {
                        loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
                        p.teleport(loc);
                    }else
                        p.teleport(ASkyBlockAPI.getInstance().getSpawnLocation());
                    return true;
                }
            }
        }else if(VoidSpawn.USkyBlock){
            uSkyBlockAPI usb = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");
            IslandInfo info = usb.getIslandInfo(p);
            if(info.getWarpLocation() != null){
                p.setFallDistance(0);
                p.teleport(info.getWarpLocation());
                return true;
            }else if(info.getIslandLocation() != null){
                p.setFallDistance(0);
                p.teleport(info.getIslandLocation());
                return true;
            }
        }
        return false;
    }
}
