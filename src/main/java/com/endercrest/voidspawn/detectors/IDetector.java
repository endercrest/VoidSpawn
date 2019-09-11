package com.endercrest.voidspawn.detectors;

import org.bukkit.entity.Player;

public interface IDetector {

    boolean isDetected(Player player, String worldName);

    String getInfo();

    String getName();
}
