package com.endercrest.voidspawn.detectors;

import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.modes.options.Option;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 5/28/2017.
 * <p>
 * This detector is for when the player is below the void or when the player is above 128 (Nether bedrock level).
 */
public class NetherDetector implements Detector {
    @Override
    public boolean isDetected(Mode mode, Player player, World world) {
        Option<Integer> offsetOption = mode.getOption(BaseMode.OPTION_OFFSET);
        int offset = offsetOption.getValue(world).orElse(0);

        return player.getLocation().getBlockY() < -offset || player.getLocation().getBlockY() >= 128 - offset;
    }

    @Override
    public String getInfo() {
        return getName() + " - Activated when entering the void or going above nether bedrock level.";
    }

    @Override
    public String getName() {
        return "Nether";
    }
}
