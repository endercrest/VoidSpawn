package com.endercrest.voidspawn.detectors;

import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.options.IntegerOption;
import com.endercrest.voidspawn.options.Option;
import com.endercrest.voidspawn.options.OptionIdentifier;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 5/28/2017.
 * <p>
 * This detector is for when the player is below the void or when the player is above 128 (Nether bedrock level).
 */
public class NetherDetector extends VoidDetector {
    public static final OptionIdentifier<Integer> OPTION_ROOF = new OptionIdentifier<>(Integer.class, "roof_height", "The height to detect at on the roof (default 128)");

    public NetherDetector() {
        attachOption(new IntegerOption(OPTION_ROOF, 128));
    }

    @Override
    public boolean isDetected(Mode mode, Player player, World world) {
        Option<Integer> voidOption = getOption(OPTION_ROOF);
        int checkHeight = voidOption.getValue(world).orElse(128);

        return player.getLocation().getBlockY() > checkHeight || super.isDetected(mode, player, world);
    }

    @Override
    public String getDescription() {
        return "Activated when entering the void or going above nether bedrock level.";
    }

    @Override
    public String getName() {
        return "Nether";
    }
}
