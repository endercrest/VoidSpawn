package com.endercrest.voidspawn;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeleportManager {
    private static TeleportManager instance = new TeleportManager();
    private VoidSpawn plugin;
    private List<UUID> playerToggle;
    private TouchUtil touchUtil;

    public static TeleportManager getInstance() {
        return instance;
    }

    public void setUp(VoidSpawn plugin) {
        this.plugin = plugin;
        playerToggle = new ArrayList<>();
        touchUtil = new TouchUtil(plugin);
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
     * @param player The player.
     * @param loc  The location of the player.
     */
    public void setPlayerLocation(Player player, Location loc) {
        touchUtil.setLocation(player, loc);
    }

    @Nullable
    public Location getPlayerLocation(Player player) {
        return touchUtil.getLocation(player);
    }

    /**
     * Teleports the player to their last touched location.
     *
     * @param p The player that will be teleported.
     * @return Whether the teleport was successful.
     */
    public TeleportResult teleportTouch(Player p) {
        Location loc = touchUtil.getLocation(p);
        if (loc == null) {
            loc = p.getLocation();
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
    }

    /** Utility useful for reading and writing touch location onto the player entity */
    private static class TouchUtil {
        private NamespacedKey worldUidKey;
        private NamespacedKey xKey;
        private NamespacedKey yKey;
        private NamespacedKey zKey;

        public TouchUtil(VoidSpawn plugin) {
            this.worldUidKey = new NamespacedKey(plugin, "touchWorldUid");
            this.xKey = new NamespacedKey(plugin, "touchX");
            this.yKey = new NamespacedKey(plugin, "touchY");
            this.zKey = new NamespacedKey(plugin, "touchZ");
        }

        public void setLocation(@NotNull Player player, @NotNull Location location) {
            PersistentDataContainer container = player.getPersistentDataContainer();

            container.set(worldUidKey, PersistentDataType.STRING, location.getWorld().getUID().toString());
            container.set(xKey, PersistentDataType.DOUBLE, location.getX());
            container.set(yKey, PersistentDataType.DOUBLE, location.getY());
            container.set(zKey, PersistentDataType.DOUBLE, location.getZ());
        }

        @Nullable
        public Location getLocation(Player player) {
            PersistentDataContainer container = player.getPersistentDataContainer();

            String worldUid = container.get(worldUidKey, PersistentDataType.STRING);
            Double locX = container.get(xKey, PersistentDataType.DOUBLE);
            Double locY = container.get(yKey, PersistentDataType.DOUBLE);
            Double locZ = container.get(zKey, PersistentDataType.DOUBLE);

            if (worldUid == null || locX == null || locY == null || locZ == null) {
                return null;
            }

            World world = Bukkit.getWorld(UUID.fromString(worldUid));
            if (world == null) {
                return null;
            }

            return new Location(world, locX, locY, locZ);
        }
    }

}