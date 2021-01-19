package com.endercrest.voidspawn.modes.flags;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class BaseFlag<T> implements Flag<T> {
    private final FlagIdentifier<T> identifier;
    private final T defaultValue;

    public BaseFlag(@NotNull FlagIdentifier<T> identifier) {
        this(identifier, null);
    }

    public BaseFlag(@NotNull FlagIdentifier<T> identifier, T defaultValue) {
        this.identifier = identifier;
        this.defaultValue = defaultValue;
    }

    public @NotNull Class<T> getType() {
        return identifier.getType();
    }

    @Override
    public @NotNull FlagIdentifier<T> getIdentifier() {
        return identifier;
    }

    @Override
    public void setValue(@NotNull World world, String value) {
        List<String> options = getOptions();
        if (value != null && options != null && !options.contains(value)) {
            throw new IllegalArgumentException("Invalid value!");
        }

        ConfigManager.getInstance().setFlag(world.getName(), getIdentifier().getName(), value);
    }

    protected Optional<T> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }
}
