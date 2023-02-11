package com.endercrest.voidspawn;

import com.endercrest.voidspawn.detectors.Detector;
import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.options.Option;
import com.endercrest.voidspawn.utils.BounceUtil;
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
                BounceMode bounceMode = BounceMode
                        .getMode(mode.getOption(BaseMode.OPTION_BOUNCE_MODE).getValue(world).orElse("count"));
                float minBounceVelocity = mode.getOption(BaseMode.OPTION_MIN_BOUNCE_VELOCITY).getValue(world)
                        .orElse(2f);
                Integer bounce = bounceTracker.get(player.getUniqueId(), () -> 0);
                if (shouldBeInBounceMode(bounceMode, bounce, bounceMax)) {
                    plugin.logDebug("In bounce mode w bounce mode: " + bounceMode + ", option bounce: " + bounceMax
                            + ", and current bounce: " + bounce);
                    // // We are going to bounce the player instead of activate the mode
                    if (player.getVelocity().getY() > 0) {
                        plugin.logDebug("Not registering bounce since already moving upwards");
                        return; // ignore when moving upwards
                    }
                    if (shouldStopBouncing(bounceMode, bounce, bounceMax, minBounceVelocity, player)) {
                        plugin.logDebug("Stopping bouncing in mode: " + bounceMode + ", option bounce: " + bounceMax
                                + ", current bounce: " + bounce + " min bounce velocity: " + minBounceVelocity);
                        bounceTracker.invalidate(player.getUniqueId());
                    } else {
                        Vector bounceVector = player.getVelocity();
                        double bounceY = getBounceYVelocity(bounceMode, bounceMax, minBounceVelocity, player);
                        plugin.logDebug("Setting bounce y-velocity: " + bounceY);
                        bounceVector.setY(bounceY);
                        player.setVelocity(bounceVector);

                        bounceTracker.put(player.getUniqueId(), bounce + 1);

                        activationTracker.put(player.getUniqueId(), Instant.now());
                        return;
                    }

                }
            } catch (ExecutionException ignored) {
            }

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

        if (result != TeleportResult.SUCCESS && instant == null)

        {
            player.sendMessage(MessageUtil.colorize("&cAn error occurred, notify an administrator."));
            plugin.log("Error while teleporting player: " + result.getMessage());
            if (mode != null) {
                player.sendMessage(MessageUtil
                        .colorize(String.format("&cDetails, World: %s, Mode: %s", worldName, mode.getName())));
                plugin.log(String.format("Details, World: %s, Mode: %s", worldName, mode.getName()));

            }
        }
    }

    private boolean shouldBeInBounceMode(BounceMode mode, int bounce, int bounceMax) {
        if (mode == BounceMode.COUNT) {
            if (bounceMax <= 0) {
                return false;
            }
            return bounce < bounceMax;
        } else if (mode == BounceMode.PHYSICS) {
            return true;
        } else {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }

    private boolean shouldStopBouncing(BounceMode mode, int bounce, int bounceMax, float minBounceVelocity,
            Player player) {
        if (mode == BounceMode.COUNT) {
            return bounce >= bounceMax;
        } else if (mode == BounceMode.PHYSICS) {
            if (bounce == 0) {
                return false; // allow first bounce
            }
            return Math.abs(player.getVelocity().getY()) < minBounceVelocity;
        } else {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }

    private double getBounceYVelocity(BounceMode mode, int configBounce, float minVelocity, Player player) {
        if (mode == BounceMode.COUNT) {
            return Math.max(minVelocity, Math.abs(player.getVelocity().getY()));
        } else if (mode == BounceMode.PHYSICS) {
            double cor = configBounce / 100.0D;
            double height = TeleportManager.getInstance().getPlayerLocation(player.getUniqueId()).getY() - player.getLocation().getY();
            return BounceUtil.getVelocityForHeight(height * cor);
        } else {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || !(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        Instant instant = activationTracker.getIfPresent(player.getUniqueId());
        if (instant == null) {
            return;
        }

        Instant future = instant.plus(500, ChronoUnit.MILLIS);
        if (Instant.now().isAfter(future)) {
            return;
        }

        event.setCancelled(true);
        activationTracker.invalidate(player.getUniqueId());
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

    public static enum BounceMode {
        COUNT, PHYSICS;

        public static BounceMode getMode(String mode) {
            if (mode.equalsIgnoreCase("count")) {
                return COUNT;
            } else if (mode.equalsIgnoreCase("physics")) {
                return PHYSICS;
            } else {
                throw new IllegalArgumentException("Unknown mode: " + mode);
            }
        }
    }
}
