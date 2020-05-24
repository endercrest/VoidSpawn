package com.endercrest.voidspawn;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
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
import world.bentobox.bentobox.api.user.User;

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
        player.setFallDistance(0);
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
                    p.setFallDistance(0);
                    p.teleport(newLoc);
                    return TeleportResult.SUCCESS;
                }
            }
            for (int i = 1; i < 10; i++) {
                Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ() + i), loc.getZ() + i);
                Location newLocBelow = new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) - 1, loc.getZ() + i);
                if (!newLocBelow.getBlock().getType().equals(Material.AIR)) {
                    p.setFallDistance(0);
                    p.teleport(newLoc);
                    return TeleportResult.SUCCESS;
                }
            }
            p.setFallDistance(0);
            p.teleport(loc.getWorld().getSpawnLocation());
            return TeleportResult.SUCCESS;
        }
        p.setFallDistance(0);
        p.teleport(loc);
        return TeleportResult.SUCCESS;
    }

    /**
     * Teleports the player to their sky block island. (Only Available if IslandWorld or ASkyBlock or uSkyBlock is installed).
     *
     * @param p The player that will be teleported.
     * @return Whether the teleport was successful.
     */
    public TeleportResult teleportIsland(Player p, String worldName) {
        TeleportResult result;
        if (VoidSpawn.IslandWorld) {
            result = teleportIslandWorld(p);
        } else if (VoidSpawn.ASkyBlock) {
            result = teleportASkyBlock(p);
        } else if (VoidSpawn.BentoBox) {
            result = teleportBentoBox(p);
        } else if (VoidSpawn.USkyBlock) {
            result = teleportUSkyBlock(p);
        } else {
            return TeleportResult.MISSING_ISLAND_DEPEND;
        }

        if (result == TeleportResult.SUCCESS) {
            return result;
        }

        if (ConfigManager.getInstance().isWorldSpawnSet(worldName)) {
            return teleportSpawn(p, worldName);
        }

        return result;
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

    private TeleportResult teleportIslandWorld(Player p) {
        if (IslandWorldApi.haveIsland(p.getName()) || IslandWorldApi.isHelpingIsland(p.getName())) {
            SimpleIsland island = IslandWorld.getInstance().getPlayerIsland(p);
            if (island == null) {
                island = IslandWorld.getInstance().getHelpingIsland(p);
            }
            if (island != null) {
                p.setFallDistance(0);
                Location loc = island.getLocation().toLocation();
                loc.setWorld(IslandWorldApi.getIslandWorld());
                p.teleport(loc);
                return TeleportResult.SUCCESS;
            }
        }
        return TeleportResult.MISSING_ISLAND;
    }

    private TeleportResult teleportASkyBlock(Player p) {
        if (ASkyBlockAPI.getInstance().hasIsland(p.getUniqueId()) || ASkyBlockAPI.getInstance().inTeam(p.getUniqueId())) {
            Location location = ASkyBlockAPI.getInstance().getHomeLocation(p.getUniqueId());
            if (location != null) {
                p.setFallDistance(0);
                p.teleport(location);
                return TeleportResult.SUCCESS;
            }
            Location loc = ASkyBlockAPI.getInstance().getIslandLocation(p.getUniqueId());
            p.setFallDistance(0);
            if (loc != null) {
                loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
                p.teleport(loc);
            } else {
                p.teleport(ASkyBlockAPI.getInstance().getSpawnLocation());
            }
            return TeleportResult.SUCCESS;
        }

        p.teleport(ASkyBlockAPI.getInstance().getSpawnLocation());
        return TeleportResult.SUCCESS;
    }

    private TeleportResult teleportBentoBox(Player p) {
        BentoBox bentoBox = (BentoBox) Bukkit.getPluginManager().getPlugin("BentoBox");

        World world = p.getWorld();

        // Check if world is an island world (this check is for a bug in before v1.13)
        if (bentoBox.getIWM().inWorld(world))
            return TeleportResult.MISSING_ISLAND;

        // First checks if spawn can be found in current world. If not, iterate through worlds until we find one.
        Location location = bentoBox.getIslands().getSafeHomeLocation(world, User.getInstance(p), 1);
        if (location == null) {
            for (World w: Bukkit.getWorlds()) {
                location = bentoBox.getIslands().getSafeHomeLocation(w, User.getInstance(p), 1);
                if (location != null) break;
            }
        }

        if (location != null) {
            p.setFallDistance(0);
            p.teleport(location);
            return TeleportResult.SUCCESS;
        }
        return TeleportResult.MISSING_ISLAND;
    }

    private TeleportResult teleportUSkyBlock(Player p) {
        uSkyBlockAPI usb = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");
        IslandInfo info = usb.getIslandInfo(p);
        if (info.getWarpLocation() != null) {
            p.setFallDistance(0);
            p.teleport(info.getWarpLocation());
            return TeleportResult.SUCCESS;
        }
        if (info.getIslandLocation() != null) {
            p.setFallDistance(0);
            p.teleport(info.getIslandLocation());
            return TeleportResult.SUCCESS;
        }

        return TeleportResult.MISSING_ISLAND;
    }
}