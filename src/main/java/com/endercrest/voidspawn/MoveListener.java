package com.endercrest.voidspawn;

import com.endercrest.voidspawn.detectors.SubDetector;
import com.endercrest.voidspawn.modes.Command;
import com.endercrest.voidspawn.modes.SubMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MoveListener implements Listener {

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        //Updates Touch Mode teleport locations.
        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()){
            TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());
        }

        if(ConfigManager.getInstance().isModeSet(worldName)){
            SubDetector detector = DetectorManager.getInstance().getWorldDetector(worldName);
            if(detector.isDetected(player, worldName)){
                if(!TeleportManager.getInstance().isPlayerToggled(player.getUniqueId())){
                    boolean success = false;

                    handleMessage(player, worldName);
                    handleInventory(player, worldName);

                    SubMode mode = ModeManager.getInstance().getWorldSubMode(worldName);
                    if(mode != null)
                        success = mode.onActivate(player, worldName);

                    handleHybridMode(mode, player, worldName);
                    handleSound(player, worldName);

                    if(!success){
                        player.sendMessage(VoidSpawn.colorize("&cAn error occurred, notify an administrator."));
                        if(mode != null)
                            player.sendMessage(VoidSpawn.colorize(String.format("&cDetails, World: %s, Mode: %s", worldName, mode.getName())));
                    }
                }

            }
        }
    }

    private void handleInventory(Player player, String world){
        if(!ConfigManager.getInstance().getKeepInventory(world)){
            player.getInventory().clear();
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            player.getInventory().setBoots(new ItemStack(Material.AIR));
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
        }
    }

    private void handleMessage(Player player, String world){
        String message = ConfigManager.getInstance().getMessage(world);
        if(!message.isEmpty())
            player.sendMessage(VoidSpawn.colorize(message));
    }

    private void handleSound(Player player, String world){
        String soundName = ConfigManager.getInstance().getSound(world);
        float volume = ConfigManager.getInstance().getVolume(world);
        float pitch = ConfigManager.getInstance().getPitch(world);

        try{
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), sound, volume, pitch);
        }catch(Exception ignored){}
    }

    private void handleHybridMode(SubMode mode, Player player, String world){
        if((!(mode instanceof Command)) && ConfigManager.getInstance().isHybrid(world)){
            SubMode cmdMode = ModeManager.getInstance().getSubMode("command");
            if(cmdMode != null)
                cmdMode.onActivate(player, world);
        }
    }
}