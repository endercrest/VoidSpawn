package com.endercrest.voidspawn.detectors;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.modes.flags.Flag;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 5/27/2017.
 * <p>
 * This is the classic mode for VoidSpawn which detects when the player enters the void.
 */
public class VoidDetector implements Detector {

    @Override
    public boolean isDetected(Mode mode, Player player, World world) {
        Flag<Integer> offsetFlag = mode.getFlag(BaseMode.FLAG_OFFSET);
        int offset = offsetFlag.getValue(world).orElse(0);

        return player.getLocation().getBlockY() < -offset;
    }

    @Override
    public String getInfo() {
        return getName() + " - [Default] Activated by entering the void.";
    }

    @Override
    public String getName() {
        return "Void";
    }
}
