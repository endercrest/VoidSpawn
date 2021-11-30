package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.options.Option;
import com.endercrest.voidspawn.options.OptionIdentifier;
import com.endercrest.voidspawn.modes.status.Status;
import com.endercrest.voidspawn.options.container.OptionContainer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface Mode extends OptionContainer {

    /**
     * Called when the player enters the void.
     *
     * @param player    The player who has entered the void and be teleported or whatever the action is.
     * @param worldName The world name in which the player resides.
     * @return Returns whether the action successfully occurred.
     */
    TeleportResult onActivate(Player player, String worldName);

    /**
     * This method is called when this mode is set in a world. This is meant to give any additional instructions should a mode require it.
     * As well as change any settings if necessary.
     *
     * @param args      The args from the command on the set.
     * @param worldName The world name where the mode was set.
     * @param p         The player who invoked the command to set the mode.
     * @return Returns whether the onSet method successfully executed and gave proper details.
     */
    default boolean onSet(String[] args, String worldName, Player p) {
        ConfigManager.getInstance().setMode(worldName, args[1]);
        return true;
    }

    /**
     * The statuses of the current mode. Used for providing information on whether this mode is configured correctly or
     * providing any other information to the user.
     *
     * @param worldName The name of world to get the status of.
     * @return Must always return a non-null array.
     */
    Status[] getStatus(String worldName);

    boolean isEnabled();

    /**
     * Get the details about the mode. This should be an unformatted string.
     * ex. "Will teleport player to set spot."
     *
     * @return Returns the string that will be displayed upon player request details of the mode.
     */
    String getDescription();

    /**
     * Get the name of the mode.
     *
     * @return The string version of the mode.
     */
    String getName();
}