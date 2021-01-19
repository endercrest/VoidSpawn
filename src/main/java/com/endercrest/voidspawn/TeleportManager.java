package com.endercrest.voidspawn;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeleportManager {
    private static TeleportManager instance = new TeleportManager();
    private VoidSpawn plugin;
    private HashMap<UUID, Location> playerLocation;
    private List<UUID> playerToggle;

    public static TeleportManager getInstance() {
        return instance;
    }

    public void setUp(VoidSpawn plugin) {
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
    public TeleportResult teleportSpawn(Player player, String worldName) {
        double x = ConfigManager.getInstance().getDouble(worldName + ".spawn.x", 0);
        double y = ConfigManager.getInstance().getDouble(worldName + ".spawn.y", 0);
        double z = ConfigManager.getInstance().getDouble(worldName + ".spawn.z", 0);
        float pitch = ConfigManager.getInstance().getFloat(worldName + ".spawn.pitch", 0);
        float yaw = ConfigManager.getInstance().getFloat(worldName + ".spawn.yaw", 0);
        World world = plugin.getServer().getWorld(ConfigManager.getInstance().getString(worldName + ".spawn.world", "world"));
        if (world == null) {
            return TeleportResult.INVALID_WORLD;
        }
        Location location = new Location(world, x, y, z, yaw, pitch);
        player.teleport(location);
        return TeleportResult.SUCCESS;
    }

    /**
     * Update the players location for touch spawn mode.
     *
     * @param uuid The UUID of the player.
     * @param loc  The location of the player.
     */
    public void setPlayerLocation(UUID uuid, Location loc) {
        playerLocation.put(uuid, loc);
    }

    public Location getPlayerLocation(UUID uuid) {
        return playerLocation.get(uuid);
    }

    /**
     * Teleports the player to their last touched location.
     *
     * @param p The player that will be teleported.
     * @return Whether the teleport was successful.
     */
    public TeleportResult teleportTouch(Player p) {
        UUID uuid = p.getUniqueId();
        Location loc;
        if (playerLocation.get(uuid) == null) {
            loc = p.getLocation();
        } else {
            loc = playerLocation.get(uuid);
        }
        Location below = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ());
        if (below.getBlock().getType().equals(Material.AIR)) {
            for (int i = 1; i < 10; i++) {
                Location newLoc = new Location(loc.getWorld(), loc.getX() + i, loc.getWorld().getHighestBlockYAt(loc.getBlockX() + i, loc.getBlockZ()), loc.getZ());
                Location newLocBelow = new Location(loc.getWorld(), loc.getX() + i, loc.getWorld().getHighestBlockYAt(loc.getBlockX() + i, loc.getBlockZ()) - 1, loc.getZ());
                if (!newLocBelow.getBlock().getType().equals(Material.AIR)) {
                    p.teleport(newLoc);
                    return TeleportResult.SUCCESS;
                }
            }
            for (int i = 1; i < 10; i++) {
                Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ() + i), loc.getZ() + i);
                Location newLocBelow = new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) - 1, loc.getZ() + i);
                if (!newLocBelow.getBlock().getType().equals(Material.AIR)) {
                    p.teleport(newLoc);
                    return TeleportResult.SUCCESS;
                }
            }
            p.teleport(loc.getWorld().getSpawnLocation());
            return TeleportResult.SUCCESS;
        }
        p.teleport(loc);
        return TeleportResult.SUCCESS;
    }

    /**
     * Checks whether the player is toggled disabled.
     *
     * @param uuid The players UUID.
     * @return Returns true if player has teleportation toggled.
     */
    public boolean isPlayerToggled(UUID uuid) {
        return playerToggle.contains(uuid);
    }

    /**
     * Toggle the current status of the player.
     *
     * @param uuid The uuid of the player.
     * @return Returns true if the player has just been toggled to disable teleportation.
     */
    public boolean togglePlayer(UUID uuid) {
        if (playerToggle.contains(uuid)) {
            playerToggle.remove(uuid);
            return false;
        } else {
            playerToggle.add(uuid);
            return true;
        }
    }

    /**
     * Disables toggle and re-enables the player to be teleported.
     *
     * @param uuid The UUID of the player.
     */
    public void disableToggle(UUID uuid) {
        playerToggle.remove(uuid);
    }

    /**
     * Enables toggle and disables the player to be teleported.
     *
     * @param uuid The UUID of the player.
     */
    public void enableToggle(UUID uuid) {
        playerToggle.add(uuid);
    }

    /**
     * Remove the player from maps to clean up resources.
     *
     * @param uuid Player UUID
     */
    public void removePlayer(UUID uuid) {
        playerToggle.remove(uuid);
        playerLocation.remove(uuid);
    }

}