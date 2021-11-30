package com.endercrest.voidspawn.options.container;

import com.endercrest.voidspawn.options.Option;
import com.endercrest.voidspawn.options.OptionIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface OptionContainer {
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
}
