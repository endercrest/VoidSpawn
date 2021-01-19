package com.endercrest.voidspawn.modes.flags;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class FloatFlag extends BaseFlag<Float> {
    public FloatFlag(FlagIdentifier<Float> name) {
        super(name);
    }

    public FloatFlag(FlagIdentifier<Float> name, Float defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Optional<Float> getValue(World world) {
        String value = ConfigManager.getInstance().getFlag(world.getName(), getIdentifier());
        if (value == null)
            return getDefaultValue();

        try {
            return Optional.of(Float.parseFloat(value));
        } catch (NumberFormatException e) {
            return getDefaultValue();
        }
    }

    @Override
    public void setValue(@NotNull World world, String value) {
        try {
            Float.parseFloat(value); // Check if number
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
