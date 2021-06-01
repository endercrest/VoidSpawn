package com.endercrest.voidspawn.modes.options;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class StringOption extends BaseOption<String> {
    public StringOption(@NotNull OptionIdentifier<String> identifier) {
        super(identifier);
    }

    @Override
    public Optional<String> getLoadedValue(@NotNull World world) {
        return Optional.ofNullable(ConfigManager.getInstance().getOption(world.getName(), getIdentifier()));
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
