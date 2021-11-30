package com.endercrest.voidspawn.options;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BooleanOption extends BaseOption<Boolean> {
    private static final List<String> options = Collections.unmodifiableList(new ArrayList<String>() {{
        add("true");
        add("false");
    }});

    public BooleanOption(OptionIdentifier<Boolean> identifier) {
        super(identifier);
    }

    public BooleanOption(OptionIdentifier<Boolean> identifier, Boolean defaultValue) {
        super(identifier, defaultValue);
    }

    @Override
    public Optional<Boolean> getLoadedValue(@NotNull World world) {
        String value = ConfigManager.getInstance().getOption(world.getName(), getIdentifier());
        if (value == null)
            return Optional.empty();

        if (options.contains(value)) {
            return Optional.of(Boolean.valueOf(value));
        }
        return Optional.empty();
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
