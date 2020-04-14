package com.endercrest.voidspawn;

import com.endercrest.voidspawn.detectors.IDetector;
import com.endercrest.voidspawn.modes.CommandMode;
import com.endercrest.voidspawn.modes.Mode;
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
    private VoidSpawn plugin;

    public VoidListener(VoidSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event) {
        TeleportResult result = TeleportResult.INCOMPLETE_MODE;
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();

        if (!ConfigManager.getInstance().isModeSet(worldName)) {
            return;
        }

        IDetector detector = DetectorManager.getInstance().getWorldDetector(worldName);
        if (!detector.isDetected(player, worldName)) {
            return;
        }

        if (!player.hasPermission("vs.enable")) {
            return;
        }

        if (TeleportManager.getInstance().isPlayerToggled(player.getUniqueId())) {
            return;
        }

        handleMessage(player, worldName);
        handleInventory(player, worldName);

        Mode mode = ModeManager.getInstance().getWorldSubMode(worldName);
        if (mode != null)
            result = mode.onActivate(player, worldName);

        handleHybridMode(mode, player, worldName);
        handleSound(player, worldName);

        if (result != TeleportResult.SUCCESS) {
            player.sendMessage(MessageUtil.colorize("&cAn error occurred, notify an administrator."));
            plugin.log("Error while teleporting player: " + result.getMessage());
            if (mode != null) {
                player.sendMessage(MessageUtil.colorize(String.format("&cDetails, World: %s, Mode: %s", worldName, mode.getName())));
                plugin.log(String.format("Details, World: %s, Mode: %s", worldName, mode.getName()));

            }
        }
    }

    private void handleInventory(Player player, String world) {
        if (!ConfigManager.getInstance().getKeepInventory(world)) {
            player.getInventory().clear();
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            player.getInventory().setBoots(new ItemStack(Material.AIR));
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
        }
    }

    private void handleMessage(Player player, String world) {
        String message = ConfigManager.getInstance().getMessage(world);
        if (!message.isEmpty())
            player.sendMessage(MessageUtil.colorize(message));
    }

    private void handleSound(Player player, String world) {
        String soundName = ConfigManager.getInstance().getSound(world);
        float volume = ConfigManager.getInstance().getVolume(world);
        float pitch = ConfigManager.getInstance().getPitch(world);

        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (Exception ignored) {
        }
    }

    private void handleHybridMode(Mode mode, Player player, String world) {
        if ((!(mode instanceof CommandMode)) && ConfigManager.getInstance().isHybrid(world)) {
            Mode cmdMode = ModeManager.getInstance().getSubMode("command");
            if (cmdMode != null)
                cmdMode.onActivate(player, world);
        }
    }
}
