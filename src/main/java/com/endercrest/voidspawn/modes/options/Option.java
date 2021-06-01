package com.endercrest.voidspawn.modes.options;

import com.endercrest.voidspawn.modes.status.Status;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface Option<T> {
    /**
     * Get the option identifer for this object.
     */
    @NotNull
    OptionIdentifier<T> getIdentifier();

    /**
     * Get the type that this option contains.
     */
    @NotNull
    Class<T> getType();

    /**
     * Get the value of the option for the given world.
     * @param world The value to retrieve the value for
     * @return Empty if value is not set.
     */
    Optional<T> getValue(World world);

    /**
     * Set the value of this option
     * @param world The world the value is being set for.
     * @param value String value will be contained with the the {@link #getOptions()}
     */
    void setValue(@NotNull World world, String value) throws IllegalArgumentException;

    /**
     * Set the value of this option
     * @param world The world the value is being set for.
     * @param args The arguments
     */
    void setValue(@NotNull World world, String[] args) throws IllegalArgumentException;

    /**
     * Specifies all the values that are acceptable as a value.
     * @return A non-null set of accepted values or null if any value is accepted.
     */
    List<String> getOptions();

    /**
     * Get description of the option.
     */
    String getDescription();

    /**
     * Get the status of this option. This is useful to get a state of the option
     * @param world The world
     * @return
     */
    @NotNull
    Status getStatus(World world);
}
