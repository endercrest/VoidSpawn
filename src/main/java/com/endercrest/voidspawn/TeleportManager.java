 package com.endercrest.voidspawn;
 
 import com.wasteofplastic.askyblock.ASkyBlockAPI;
 import java.util.HashMap;
 import java.util.UUID;
 import org.bukkit.Bukkit;
 import org.bukkit.Location;
 import org.bukkit.Material;
 import org.bukkit.Server;
 import org.bukkit.World;
 import org.bukkit.block.Block;
 import org.bukkit.entity.Player;
 import org.bukkit.plugin.PluginManager;
 import pl.islandworld.IslandWorld;
 import pl.islandworld.api.IslandWorldApi;
 import pl.islandworld.entity.MyLocation;
 import pl.islandworld.entity.SimpleIsland;
 import us.talabrek.ultimateskyblock.api.IslandInfo;
 import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;
 
 public class TeleportManager
 {
   private VoidSpawn plugin;
   private static TeleportManager instance = new TeleportManager();
   private HashMap<UUID, Location> playerLocation = new HashMap();
   
   public static TeleportManager getInstance() {
     return instance;
   }
   
   public void setUp(VoidSpawn plugin) {
     this.plugin = plugin;
   }
   
 
 
 
 
 
   public boolean teleportSpawn(Player player, String worldName)
   {
     double x = ConfigManager.getInstance().getDouble(worldName + ".spawn.x");
     double y = ConfigManager.getInstance().getDouble(worldName + ".spawn.y");
     double z = ConfigManager.getInstance().getDouble(worldName + ".spawn.z");
     float pitch = ConfigManager.getInstance().getFloat(worldName + ".spawn.pitch");
     float yaw = ConfigManager.getInstance().getFloat(worldName + ".spawn.yaw");
     World world = this.plugin.getServer().getWorld(ConfigManager.getInstance().getString(worldName + ".spawn.world"));
     
     Location location = new Location(world, x, y, z, yaw, pitch);
     player.setFallDistance(0.0F);
     player.teleport(location);
     return true;
   }
   
 
 
 
 
   public void setPlayerLocation(UUID uuid, Location loc)
   {
     this.playerLocation.put(uuid, loc);
   }
   
 
 
 
 
   public boolean teleportTouch(Player p)
   {
     UUID uuid = p.getUniqueId();
     Location loc;
     if (this.playerLocation.get(uuid) == null) {
       loc = p.getLocation();
     } else {
       loc = (Location)this.playerLocation.get(uuid);
     }
     Location below = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1.0D, loc.getZ());
     if (below.getBlock().getType().equals(Material.AIR)) {
       for (int i = 1; i < 10; i++) {
         Location newLoc = new Location(loc.getWorld(), loc.getX() + i, loc.getWorld().getHighestBlockYAt(loc.getBlockX() + i, loc.getBlockZ()), loc.getZ());
         Location newLocBelow = new Location(loc.getWorld(), loc.getX() + i, loc.getWorld().getHighestBlockYAt(loc.getBlockX() + i, loc.getBlockZ()) - 1, loc.getZ());
         if (!newLocBelow.getBlock().getType().equals(Material.AIR)) {
           p.setFallDistance(0.0F);
           p.teleport(newLoc);
           return true;
         }
       }
       for (int i = 1; i < 10; i++) {
         Location newLoc = new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ() + i), loc.getZ() + i);
         Location newLocBelow = new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) - 1, loc.getZ() + i);
         if (!newLocBelow.getBlock().getType().equals(Material.AIR)) {
           p.setFallDistance(0.0F);
           p.teleport(newLoc);
           return true;
         }
       }
       p.setFallDistance(0.0F);
       p.teleport(loc.getWorld().getSpawnLocation());
       return true;
     }
     p.setFallDistance(0.0F);
     p.teleport(loc);
     return true;
   }
   
 
 
 
 
   public boolean teleportIsland(Player p)
   {
     if (VoidSpawn.IslandWorld) {
       if ((IslandWorldApi.haveIsland(p.getName())) || (IslandWorldApi.isHelpingIsland(p.getName()))) {
         SimpleIsland island = IslandWorld.getInstance().getPlayerIsland(p);
         if (island != null) {
           p.setFallDistance(0.0F);
           Location loc = island.getLocation().toLocation();
           loc.setWorld(IslandWorldApi.getIslandWorld());
           p.teleport(loc);
           return true;
         }
         island = IslandWorld.getInstance().getHelpingIsland(p);
         if (island != null) {
           p.setFallDistance(0.0F);
           Location loc = island.getLocation().toLocation();
           loc.setWorld(IslandWorldApi.getIslandWorld());
           p.teleport(loc);
           return true;
         }
       }
     }
     else if (VoidSpawn.ASkyBlock) {
       if ((ASkyBlockAPI.getInstance().hasIsland(p.getUniqueId())) || (ASkyBlockAPI.getInstance().inTeam(p.getUniqueId()))) {
         Location location = ASkyBlockAPI.getInstance().getHomeLocation(p.getUniqueId());
         if (location != null) {
           p.setFallDistance(0.0F);
           p.teleport(location);
           return true;
         }
         Location loc = ASkyBlockAPI.getInstance().getIslandLocation(p.getUniqueId());
         p.setFallDistance(0.0F);
         if (loc != null) {
           loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
           p.teleport(loc);
         } else {
           p.teleport(ASkyBlockAPI.getInstance().getSpawnLocation()); }
         return true;
       }
     }
     else if (VoidSpawn.USkyBlock) {
       uSkyBlockAPI usb = (uSkyBlockAPI)Bukkit.getPluginManager().getPlugin("uSkyBlock");
       IslandInfo info = usb.getIslandInfo(p);
       if (info.getWarpLocation() != null) {
         p.setFallDistance(0.0F);
         p.teleport(info.getWarpLocation());
         return true; }
       if (info.getIslandLocation() != null) {
         p.setFallDistance(0.0F);
         p.teleport(info.getIslandLocation());
         return true;
       }
     }
     return false;
   }
 }