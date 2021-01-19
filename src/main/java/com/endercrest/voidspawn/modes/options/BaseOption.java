package com.endercrest.voidspawn.modes.options;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class BaseOption<T> implements Option<T> {
    private final OptionIdentifier<T> identifier;
    private final T defaultValue;

    public BaseOption(@NotNull OptionIdentifier<T> identifier) {
        this(identifier, null);
    }

    public BaseOption(@NotNull OptionIdentifier<T> identifier, T defaultValue) {
        this.identifier = identifier;
        this.defaultValue = defaultValue;
    }

    public @NotNull Class<T> getType() {
        return identifier.getType();
    }

    @Override
    public @NotNull OptionIdentifier<T> getIdentifier() {
        return identifier;
    }

    @Override
    public void setValue(@NotNull World world, String value) {
        List<String> options = getOptions();
        if (value != null && options != null && !options.contains(value)) {
            throw new IllegalArgumentException("Invalid value!");
        }

        ConfigManager.getInstance().setOption(world.getName(), getIdentifier().getName(), value);
    }

    protected Optional<T> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    @Override
    public String getDescription() {
        return identifier.getDescription();
    }
}
