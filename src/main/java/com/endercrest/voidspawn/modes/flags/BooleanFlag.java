package com.endercrest.voidspawn.modes.flags;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BooleanFlag extends BaseFlag<Boolean> {
    private static final List<String> options = Collections.unmodifiableList(new ArrayList<String>() {{
        add("true");
        add("false");
    }});

    public BooleanFlag(FlagIdentifier<Boolean> identifier) {
        super(identifier);
    }

    public BooleanFlag(FlagIdentifier<Boolean> identifier, Boolean defaultValue) {
        super(identifier, defaultValue);
    }

    @Override
    public Optional<Boolean> getValue(World world) {
        String value = ConfigManager.getInstance().getFlag(world.getName(), getIdentifier());
        if (value == null)
            return getDefaultValue();

        if (options.contains(value)) {
            return Optional.of(Boolean.valueOf(value));
        }
        return getDefaultValue();
    }

    @Override
    public void setValue(@NotNull World world, String[] args) throws IllegalArgumentException {
        setValue(world, String.join(" ", args));
    }

    @Override
    public List<String> getOptions() {
        return options;
    }
}
