package com.endercrest.voidspawn;

import com.endercrest.voidspawn.detectors.Detector;
import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.modes.flags.Flag;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Handles detecting if a player has entered the void & calls
 * all the necessary executors.
 */
public class VoidListener implements Listener {
    private final VoidSpawn plugin;
    private final Cache<UUID, Instant> activationTracker = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    public VoidListener(VoidSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event) {
        TeleportResult result = TeleportResult.INCOMPLETE_MODE;
        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();

        if (!ConfigManager.getInstance().isModeSet(worldName)) {
            return;
        }

        Mode mode = ModeManager.getInstance().getWorldMode(worldName);

        Detector detector = DetectorManager.getInstance().getWorldDetector(worldName);
        if (!detector.isDetected(mode, player, world)) {
            return;
        }

        if (!player.hasPermission("vs.enable")) {
            return;
        }

        if (TeleportManager.getInstance().isPlayerToggled(player.getUniqueId())) {
            return;
        }


        Instant instant = activationTracker.getIfPresent(player.getUniqueId());
        if (mode != null) {
            activateMessage(mode, player, world);
            activateInventoryClear(mode, player, world);

            result = mode.onActivate(player, worldName);
            if (result == TeleportResult.SUCCESS) {
                activationTracker.put(player.getUniqueId(), Instant.now());
            }

            activateSound(mode, player, world);
            activateHybrid(mode, player, world);
        }


        if (result != TeleportResult.SUCCESS && instant == null) {
            player.sendMessage(MessageUtil.colorize("&cAn error occurred, notify an administrator."));
            plugin.log("Error while teleporting player: " + result.getMessage());
            if (mode != null) {
                player.sendMessage(MessageUtil.colorize(String.format("&cDetails, World: %s, Mode: %s", worldName, mode.getName())));
                plugin.log(String.format("Details, World: %s, Mode: %s", worldName, mode.getName()));

            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || !(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        Instant instant = activationTracker.getIfPresent(player.getUniqueId());
        if (instant == null)
            return;

        Instant future = instant.plus(500, ChronoUnit.MILLIS);
        if (Instant.now().isAfter(future))
            return;

        event.setCancelled(true);
    }

    private void activateHybrid(Mode mode, Player player, World world) {
        Flag<Boolean> hybridFlag = mode.getFlag(BaseMode.FLAG_HYBRID);
        if (hybridFlag.getValue(world).orElse(false)) {
            Mode cmdMode = ModeManager.getInstance().getMode("command");
            if (cmdMode != null)
                cmdMode.onActivate(player, world.getName());
        }
    }

    private void activateSound(Mode mode, Player player, World world) {
        Flag<Sound> soundFlag = mode.getFlag(BaseMode.FLAG_SOUND);

        Optional<Sound> sound = soundFlag.getValue(world);
        if (sound.isPresent()) {
            Flag<Float> volumeFlag = mode.getFlag(BaseMode.FLAG_SOUND_VOLUME);
            Flag<Float> pitchFlag = mode.getFlag(BaseMode.FLAG_SOUND_PITCH);

            Float volume = volumeFlag.getValue(world).orElse(1.0f);
            Float pitch = pitchFlag.getValue(world).orElse(1.0f);
            player.playSound(player.getLocation(), sound.get(), volume, pitch);
        }
    }

    private void activateInventoryClear(Mode mode, Player player, World world) {
        Flag<Boolean> keepInvFlag = mode.getFlag(BaseMode.FLAG_KEEP_INVENTORY);
        if (!keepInvFlag.getValue(world).orElse(true)) {
            player.getInventory().clear();
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            player.getInventory().setBoots(new ItemStack(Material.AIR));
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
        }
    }

    private void activateMessage(Mode mode, Player player, World world) {
        Flag<String> messageFlag = mode.getFlag(BaseMode.FLAG_MESSAGE);
        messageFlag.getValue(world)
                .ifPresent(msg -> player.sendMessage(MessageUtil.colorize(msg)));
    }
}
