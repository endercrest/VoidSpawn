package com.endercrest.voidspawn;

import com.wasteofplastic.askyblock.ASkyBlockAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pl.islandworld.IslandWorld;
import pl.islandworld.api.IslandWorldApi;
import pl.islandworld.entity.SimpleIsland;
import us.talabrek.ultimateskyblock.api.IslandInfo;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;

public class TeleportManager {
    private VoidSpawn plugin;
    private static TeleportManager instance = new TeleportManager();
    private HashMap<UUID, Location> playerLocation;
    private List<UUID> playerToggle;

    public static TeleportManager getInstance(){
        return instance;
    }

    public void setUp(VoidSpawn plugin){
        this.plugin = plugin;
        playerLocation = new HashMap<>();
        playerToggle = new ArrayList<>();
    }

    /**
     * Teleport the player to the selected world.
     *
     * @param player    The Player that will be teleported.
     * @param worldName Name of the world to get coordinates from.
     * @return Whether the teleport was successful.
     */
    public boolean teleportSpawn(Player player, String worldName){
        double x = ConfigManager.getInstance().getDouble(worldName + ".spawn.x", 0);
        double y = ConfigManager.getInstance().getDouble(worldName + ".spawn.y", 0);
        double z = ConfigManager.getInstance().getDouble(worldName + ".spawn.z", 0);
        float pitch = ConfigManager.getInstance().getFloat(worldName + ".spawn.pitch", 0);
        float yaw = ConfigManager.getInstance().getFloat(worldName + ".spawn.yaw", 0);
        World world = plugin.getServer().getWorld(ConfigManager.getInstance().getString(worldName + ".spawn.world", "world"));
        Location location = new Location(world, x, y, z, yaw, pitch);
        player.setFallDistance(0);
        player.teleport(location);
        return true;
    }

    /**
     * Update the players location for touch spawn mode.
     *
     * @param uuid The UUID of the player.
     * @param loc  The location of the player.
     */
    public void setPlayerLocation(UUID uuid, Location loc){
        playerLocation.put(uuid, loc);
    }

    /**
     * Teleports the player to their last touched location.
     *
     * @param p The player that will be teleported.
     * @return Whether the teleport was successful.
     */
    public boolean teleportTouch(Player p){
        UUID uuid = p.getUniqueId();
        Location loc;
        if(playerLocation.get(uuid) == null){
            loc = p.getLocation();
        }else{
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
     * Teleports the player to their sky block island. (Only Available if IslandWorld or ASkyBlock or uSkyBlock is installed).
     *
     * @param p The player that will be teleported.
     * @return Whether the teleport was successful.
     */
    public boolean teleportIsland(Player p, String worldName){
        if(VoidSpawn.IslandWorld){
            if(IslandWorldApi.haveIsland(p.getName()) || IslandWorldApi.isHelpingIsland(p.getName())){
                SimpleIsland island = IslandWorld.getInstance().getPlayerIsland(p);
                if(island != null){
                    return islandWorldTeleport(island, p);
                }
                island = IslandWorld.getInstance().getHelpingIsland(p);
                if(island != null){
                    return islandWorldTeleport(island, p);
                }
            }
        }else if(VoidSpawn.ASkyBlock){
            if(ASkyBlockAPI.getInstance().hasIsland(p.getUniqueId()) || ASkyBlockAPI.getInstance().inTeam(p.getUniqueId())){
                Location location = ASkyBlockAPI.getInstance().getHomeLocation(p.getUniqueId());
                if(location != null){
                    p.setFallDistance(0);
                    p.teleport(location);
                    return true;
                }
                Location loc = ASkyBlockAPI.getInstance().getIslandLocation(p.getUniqueId());
                p.setFallDistance(0);
                if(loc != null){
                    loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
                    p.teleport(loc);
                }else{
                    p.teleport(ASkyBlockAPI.getInstance().getSpawnLocation());
                }
                return true;
            }
        } else if(VoidSpawn.BentoBox) {
            BentoBox bentoBox = (BentoBox) Bukkit.getPluginManager().getPlugin("BentoBox");
            // First checks if current world is an island world. If not, iterate through worlds until we find one.
            World world = p.getWorld();
            Island island = bentoBox.getIslands().getIsland(world, p.getUniqueId());
            if(island != null) {
                p.setFallDistance(0);
                p.teleport(island.getSpawnPoint(World.Environment.NORMAL));
                return true;
            }

            for(World w: Bukkit.getWorlds()) {
                island = bentoBox.getIslands().getIsland(w, p.getUniqueId());
                if(island != null) {
                    p.setFallDistance(0);
                    p.teleport(island.getSpawnPoint(World.Environment.NORMAL));
                    return true;
                }
            }
        } else if(VoidSpawn.USkyBlock){
            uSkyBlockAPI usb = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");
            IslandInfo info = usb.getIslandInfo(p);
            if(info.getWarpLocation() != null){
                p.setFallDistance(0);
                p.teleport(info.getWarpLocation());
                return true;
            }
            if(info.getIslandLocation() != null){
                p.setFallDistance(0);
                p.teleport(info.getIslandLocation());
                return true;
            }
        }

        if(ConfigManager.getInstance().isWorldSpawnSet(worldName)){
            return teleportSpawn(p, worldName);
        }
        return false;
    }

    private boolean islandWorldTeleport(SimpleIsland island, Player p){
        p.setFallDistance(0);
        Location loc = island.getLocation().toLocation();
        loc.setWorld(IslandWorldApi.getIslandWorld());
        p.teleport(loc);
        return true;
    }

    /**
     * Checks whether the player is toggled disabled.
     * @param uuid The players UUID.
     * @return Returns true if player has teleportation toggled.
     */
    public boolean isPlayerToggled(UUID uuid){
        return playerToggle.contains(uuid);
    }

    /**
     * Toggle the current status of the player.
     * @param uuid The uuid of the player.
     * @return Returns true if the player has just been toggled to disable teleportation.
     */
    public boolean togglePlayer(UUID uuid){
        if(playerToggle.contains(uuid)){
            playerToggle.remove(uuid);
            return false;
        }else{
            playerToggle.add(uuid);
            return true;
        }
    }

    /**
     * Disables toggle and re-enables the player to be teleported.
     * @param uuid The UUID of the player.
     */
    public void disableToggle(UUID uuid){
        playerToggle.remove(uuid);
    }

    /**
     * Enables toggle and disables the player to be teleported.
     * @param uuid The UUID of the player.
     */
    public void enableToggle(UUID uuid){
        playerToggle.add(uuid);
    }

    /**
     * Remove the player from maps to clean up resources.
     * @param uuid Player UUID
     */
    public void removePlayer(UUID uuid) {
        playerToggle.remove(uuid);
        playerLocation.remove(uuid);
    }
}