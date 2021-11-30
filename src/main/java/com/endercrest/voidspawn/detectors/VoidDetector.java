package com.endercrest.voidspawn.detectors;

import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.options.IntegerOption;
import com.endercrest.voidspawn.options.Option;
import com.endercrest.voidspawn.options.OptionIdentifier;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 5/27/2017.
 * <p>
 * This is the classic mode for VoidSpawn which detects when the player enters the void.
 */
public class VoidDetector extends BaseDetector {
    @Override
    public boolean isDetected(Mode mode, Player player, World world) {
        Option<Integer> voidOption = getOption(BaseDetector.OPTION_VOID);
        int checkHeight = voidOption.getValue(world).orElse(world.getMinHeight());

        return player.getLocation().getBlockY() < checkHeight;
    }

    @Override
    public String getDescription() {
        return "Activated by entering the void. [Default]";
    }

    @Override
    public String getName() {
        return "Void";
    }
}
