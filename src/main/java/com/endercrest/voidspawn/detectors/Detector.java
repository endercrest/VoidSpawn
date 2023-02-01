package com.endercrest.voidspawn.detectors;

import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.options.container.OptionContainer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface Detector extends OptionContainer {

    /**
     * Checks if the current player is within the 'detected' region. This is used to signal to the mode whether it
     * should activate
     * @param mode The current mode
     * @param player The player
     * @param world The world
     */
    boolean isDetected(Mode mode, Player player, World world);

    String getDescription();

    /**
     * Get the name of the detector.
     *
     * @return The string version of the mode.
     */
    String getName();
}
