package com.endercrest.voidspawn.modes.flags;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class StringFlag extends BaseFlag<String> {
    public StringFlag(@NotNull FlagIdentifier<String> identifier) {
        super(identifier);
    }

    @Override
    public Optional<String> getValue(World world) {
        return Optional.ofNullable(ConfigManager.getInstance().getFlag(world.getName(), getIdentifier()));
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
