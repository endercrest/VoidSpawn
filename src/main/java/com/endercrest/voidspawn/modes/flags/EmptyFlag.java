package com.endercrest.voidspawn.modes.flags;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class EmptyFlag<T> extends BaseFlag<T> {
    public EmptyFlag(@NotNull FlagIdentifier<T> identifier) {
        super(identifier);
    }

    @Override
    public Optional<T> getValue(World world) {
        return Optional.empty();
    }

    @Override
    public void setValue(@NotNull World world, String value) {}

    @Override
    public void setValue(@NotNull World world, String[] args) throws IllegalArgumentException {}

    @Override
    public List<String> getOptions() {
        return Collections.emptyList();
    }
}
