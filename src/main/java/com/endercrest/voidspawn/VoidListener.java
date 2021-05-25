package com.endercrest.voidspawn;

import com.endercrest.voidspawn.detectors.Detector;
import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.modes.options.Option;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
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
    private final Cache<UUID, Integer> bounceTracker = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
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
            activationTracker.invalidate(player.getUniqueId());
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
            try {
                Integer bounceMax = mode.getOption(BaseMode.OPTION_BOUNCE).getValue(world).orElse(0);
                if (bounceMax > 0) {

                    Integer bounce = bounceTracker.get(player.getUniqueId(), () -> 0);
                    if (bounce < bounceMax) {
                        // We are going to bounce the player instead of activate the mode
                        float minBounceVelocity = mode.getOption(BaseMode.OPTION_MIN_BOUNCE_VELOCITY).getValue(world).orElse(2f);

                        Vector bounceVector = player.getVelocity();
                        bounceVector.setY(Math.max(Math.abs(bounceVector.getY()), minBounceVelocity));
                        player.setVelocity(bounceVector);

                        if (instant == null || Instant.now().isAfter(instant.plus(2, ChronoUnit.SECONDS))) {
                            player.sendMessage("Bounce!" + player.getVelocity());
                            bounceTracker.put(player.getUniqueId(), bounce + 1);
                        }

                        activationTracker.put(player.getUniqueId(), Instant.now());
                        return;
                    }

                    bounceTracker.invalidate(player.getUniqueId());
                }
            } catch (ExecutionException ignored) {}

            activateMessage(mode, player, world);
            activateInventoryClear(mode, player, world);

            result = mode.onActivate(player, worldName);
            if (result == TeleportResult.SUCCESS) {
                activationTracker.put(player.getUniqueId(), Instant.now());
            }

            activateSound(mode, player, world);
            activateHybrid(mode, player, world);
            activateDeathIncrement(mode, player, world);
            activateDamage(mode, player, world);
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
        Option<Boolean> hybridOption = mode.getOption(BaseMode.OPTION_HYBRID);
        if (hybridOption.getValue(world).orElse(false)) {
            Mode cmdMode = ModeManager.getInstance().getMode("command");
            if (cmdMode != null)
                cmdMode.onActivate(player, world.getName());
        }
    }

    private void activateSound(Mode mode, Player player, World world) {
        Option<Sound> soundOption = mode.getOption(BaseMode.OPTION_SOUND);

        Optional<Sound> sound = soundOption.getValue(world);
        if (sound.isPresent()) {
            Option<Float> volumeOption = mode.getOption(BaseMode.OPTION_SOUND_VOLUME);
            Option<Float> pitchOption = mode.getOption(BaseMode.OPTION_SOUND_PITCH);

            Float volume = volumeOption.getValue(world).orElse(1.0f);
            Float pitch = pitchOption.getValue(world).orElse(1.0f);
            player.playSound(player.getLocation(), sound.get(), volume, pitch);
        }
    }

    private void activateInventoryClear(Mode mode, Player player, World world) {
        Option<Boolean> keepInvOption = mode.getOption(BaseMode.OPTION_KEEP_INVENTORY);
        if (!keepInvOption.getValue(world).orElse(true)) {
            player.getInventory().clear();
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            player.getInventory().setBoots(new ItemStack(Material.AIR));
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            player.getInventory().setLeggings(new ItemStack(Material.AIR));
        }
    }

    private void activateMessage(Mode mode, Player player, World world) {
        Option<String> messageOption = mode.getOption(BaseMode.OPTION_MESSAGE);
        messageOption.getValue(world)
                .ifPresent(msg -> player.sendMessage(MessageUtil.colorize(msg)));
    }

    private void activateDeathIncrement(Mode mode, Player player, World world) {
        if (mode.getOption(BaseMode.OPTION_INC_DEATH_STAT).getValue(world).orElse(false)) {
            player.incrementStatistic(Statistic.DEATHS, 1);
        }
    }

    private void activateDamage(Mode mode, Player player, World world) {
        int damage = mode.getOption(BaseMode.OPTION_DAMAGE).getValue(world).orElse(0);
        if (damage > 0) {
            player.damage(damage);
        }
    }
}
