package com.endercrest.voidspawn.detectors;

import com.endercrest.voidspawn.modes.Mode;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface Detector {

    boolean isDetected(Mode mode, Player player, World world);

    String getInfo();

    String getName();
}
