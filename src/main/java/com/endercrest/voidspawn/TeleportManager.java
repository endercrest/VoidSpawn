package com.endercrest.voidspawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pl.islandworld.IslandWorld;
import pl.islandworld.entity.MyLocation;

import java.util.HashMap;
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

    public void teleportSpawn(String worldName, Player player){
        double x = ConfigManager.getInstance().getDouble(worldName + ".x");
        double y = ConfigManager.getInstance().getDouble(worldName + ".y");
        double z = ConfigManager.getInstance().getDouble(worldName + ".z");
        float pitch = ConfigManager.getInstance().getFloat(worldName + ".pitch");
        float yaw = ConfigManager.getInstance().getFloat(worldName + ".yaw");
        World world = plugin.getServer().getWorld(ConfigManager.getInstance().getString(worldName + ".world"));

        Location location = new Location(world, x, y, z, yaw, pitch);
        player.setFallDistance(0);
        player.teleport(location);
    }

    public void teleportSpawn(Player player){
        String worldName = player.getWorld().getName();
        double x = ConfigManager.getInstance().getDouble(worldName + ".x");
        double y = ConfigManager.getInstance().getDouble(worldName + ".y");
        double z = ConfigManager.getInstance().getDouble(worldName + ".z");
        float pitch = ConfigManager.getInstance().getFloat(worldName + ".pitch");
        float yaw = ConfigManager.getInstance().getFloat(worldName + ".yaw");
        World world = plugin.getServer().getWorld(ConfigManager.getInstance().getString(worldName + ".world"));

        Location location = new Location(world, x, y, z, yaw, pitch);
        player.setFallDistance(0);
        player.teleport(location);
    }

    public void setPlayerLocation(UUID uuid, Location loc){
        playerLocation.put(uuid, loc);
    }

    public void teleportTouch(Player p){
        UUID uuid = p.getUniqueId();
        p.teleport(playerLocation.get(uuid));
    }

    public void teleportIsland(Player p){
        MyLocation coords = IslandWorld.getInstance().getPlayerIsland(p.getName()).getLocation();
        Location location = new Location(IslandWorld.getInstance().getIslandWorld(), coords.getX(), coords.getY(), coords.getZ());
        p.teleport(location);
    }
}
