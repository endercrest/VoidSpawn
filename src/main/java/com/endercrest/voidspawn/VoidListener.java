package com.endercrest.voidspawn;

import com.endercrest.voidspawn.detectors.SubDetector;
import com.endercrest.voidspawn.modes.Command;
import com.endercrest.voidspawn.modes.SubMode;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Handles detecting if a player has entered the void & calls
 * all the necessary executors.
 */
public class VoidListener implements Listener {

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event) {
        boolean success = false;
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        if(!player.hasPermission("vs.enable")) {
            return;
        }

        if(!ConfigManager.getInstance().isModeSet(worldName)) {
            return;
        }

        SubDetector detector = DetectorManager.getInstance().getWorldDetector(worldName);
        if(!detector.isDetected(player, worldName)) {
            return;
        }

        if(TeleportManager.getInstance().isPlayerToggled(player.getUniqueId())) {
            return;
        }

        handleMessage(player, worldName);
        handleInventory(player, worldName);

        SubMode mode = ModeManager.getInstance().getWorldSubMode(worldName);
        if(mode != null)
            success = mode.onActivate(player, worldName);

        handleHybridMode(mode, player, worldName);
        handleSound(player, worldName);

        if(!success){
            player.sendMessage(MessageUtil.colorize("&cAn error occurred, notify an administrator."));
            if(mode != null)
                player.sendMessage(MessageUtil.colorize(String.format("&cDetails, World: %s, Mode: %s", worldName, mode.getName())));
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
            player.sendMessage(MessageUtil.colorize(message));
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
