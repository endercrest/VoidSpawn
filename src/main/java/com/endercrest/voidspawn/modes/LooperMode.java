package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.options.BooleanOption;
import com.endercrest.voidspawn.options.IntegerOption;
import com.endercrest.voidspawn.options.OptionIdentifier;
import com.endercrest.voidspawn.modes.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Optional;

public class LooperMode extends BaseMode {
    public static final OptionIdentifier<Integer> OPTION_VERTICAL_OFFSET = new OptionIdentifier<>(Integer.class, "vertical_offset", "The offset from the top of world to teleport the player");
    public static final OptionIdentifier<Boolean> OPTION_KEEP_VELOCITY = new OptionIdentifier<>(Boolean.class, "keep_velocity", "Whether the players velocity should be maintained after being teleported");

    public LooperMode() {
        attachOption(new IntegerOption(OPTION_VERTICAL_OFFSET));
        attachOption(new BooleanOption(OPTION_KEEP_VELOCITY, true));
    }

    @Override
    public TeleportResult onActivate(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null)
            return TeleportResult.INVALID_WORLD;

        Optional<Integer> verticalOffset = getOption(OPTION_VERTICAL_OFFSET).getValue(world);


        Location location = player.getLocation();
        location.setY(world.getMaxHeight());
        verticalOffset.ifPresent(integer -> location.setY(location.getY() + integer));

        Vector velocity = player.getVelocity().clone();

        player.teleport(location);

        if (getOption(OPTION_KEEP_VELOCITY).getValue(world).orElse(true))
            player.setVelocity(velocity);

        return TeleportResult.SUCCESS;
    }

    @Override
    public Status[] getStatus(String worldName) {
        return new Status[0];
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Will teleport player to the top of the world at the same x,z location.";
    }

    @Override
    public String getName() {
        return "Looper";
    }
}
