package com.endercrest.voidspawn.options;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.modes.status.Status;
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

    @Override
    public Optional<T> getValue(@NotNull World world) {
        Optional<T> val = getLoadedValue(world);

        return val.isPresent() ? val : getDefaultValue();
    }

    /**
     * Retrieve the value from the config, should return an empty optional if not set
     *
     * @param world The world to retrieve the value for
     */
    protected abstract Optional<T> getLoadedValue(@NotNull World world);

    private Optional<T> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }

    @Override
    public String getDescription() {
        return identifier.getDescription();
    }

    @Override
    public @NotNull Status getStatus(World world) {
        boolean hasValue = getLoadedValue(world).isPresent();
        Optional<T> value = getValue(world);

        String valueText = value.map(v -> String.format(" (Value: '%s')", v)).orElse("");
        String text = String.format("Key: '%s' %s", identifier.getName(), valueText);

        return new Status(hasValue ? Status.Type.COMPLETE : Status.Type.UNSET, text);
    }
}
