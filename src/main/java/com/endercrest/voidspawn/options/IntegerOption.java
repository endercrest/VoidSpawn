package com.endercrest.voidspawn.options;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class IntegerOption extends BaseOption<Integer> {
    public IntegerOption(@NotNull OptionIdentifier<Integer> identifier) {
        super(identifier);
    }

    public IntegerOption(@NotNull OptionIdentifier<Integer> identifier, Integer defaultValue) {
        super(identifier, defaultValue);
    }

    @Override
    public Optional<Integer> getLoadedValue(@NotNull World world) {
        String value = ConfigManager.getInstance().getOption(world.getName(), getIdentifier());
        if (value == null)
            return Optional.empty();

        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public void setValue(@NotNull World world, String value) {
        if (value == null) {
            super.setValue(world, (String) null);
            return;
        }

        try {
            Integer.parseInt(value); // Check if number
            super.setValue(world, value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Must be a number");
        }
    }

    @Override
    public void setValue(@NotNull World world, String[] args) throws IllegalArgumentException {
        setValue(world, String.join(" ", args));
    }

    @Override
    public List<String> getOptions() {
        return null;
    }
}
