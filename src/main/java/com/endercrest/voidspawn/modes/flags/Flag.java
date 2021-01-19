package com.endercrest.voidspawn.modes.flags;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface Flag<T> {

    /**
     * Constant that signifies that this flag accepts an open value.
     */
    String OPEN_VALUE_CONSTANT = "__OpenValue__";

    /**
     *
     * @return
     */
    @NotNull
    FlagIdentifier<T> getIdentifier();

    /**
     *
     * @return
     */
    @NotNull
    Class<T> getType();

    /**
     *
     * @param world
     * @return
     */
    Optional<T> getValue(World world);

    /**
     * Set the value of this flag
     * @param world The world the value is being set for.
     * @param value String value will be contained with the the {@link #getOptions()}
     */
    void setValue(@NotNull World world, String value) throws IllegalArgumentException;

    /**
     * Set the value of this flag
     * @param world The world the value is being set for.
     * @param args The arguments
     */
    void setValue(@NotNull World world, String[] args) throws IllegalArgumentException;

    /**
     * Specifies all the values that are acceptable as a value.
     * @return A non-null set of accepted values or null if any value is accepted.
     */
    List<String> getOptions();
}
