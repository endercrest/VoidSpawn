package com.endercrest.voidspawn.detectors;

import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 5/27/2017.
 *
 * SubDetector is
 */
public interface SubDetector {

    boolean isDetected(Player player, String worldName);

    String getInfo();

    String getName();
}
