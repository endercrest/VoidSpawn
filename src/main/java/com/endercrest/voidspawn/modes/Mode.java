package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.modes.options.Option;
import com.endercrest.voidspawn.modes.options.OptionIdentifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface Mode {

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
     * Add deals about the mode and is displayed when the player asks for the list of mods.
     * ex. "&6Spawn &f- Will teleport player to set spot."
     *
     * @return Returns the string that will be displayed upon player request details of the mode.
     */
    String getHelp();

    /**
     * Get the name of the mode.
     *
     * @return The string version of the mode.
     */
    String getName();

    /**
     * Attempt to retrieve a option of the given name.
     *
     * @param <T>        The type the option will be casted to.
     * @param identifier The identifier for the option.
     * @return The option if it exists, returns empty if no existing option or
     * if the option identifier type does not match option type.
     */
    @NotNull <T> Option<T> getOption(OptionIdentifier<T> identifier);

    /**
     * Attempt to retrieve a option of the given name by the name. If possible, use {@link #getOption(OptionIdentifier)} as
     * that guarantees a type.
     *
     * @param name The name of the option to retrieve.
     * @return The option or null if doesn't exist.
     */
    @Nullable Option<?> getOption(String name);

    /**
     * Get a list of all options for this mode.
     *
     * @return non-null list of options
     */
    Collection<Option<?>> getOptions();

    enum StatusType {
        COMPLETE,
        INCOMPLETE,
        INFO,
    }

    class Status {
        private StatusType type;
        private String message;

        Status(StatusType type, String message) {
            this.type = type;
            this.message = message;
        }

        public StatusType getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }
    }
}