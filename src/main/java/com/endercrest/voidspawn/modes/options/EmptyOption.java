package com.endercrest.voidspawn.modes.options;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class EmptyOption<T> extends BaseOption<T> {
    public EmptyOption(@NotNull OptionIdentifier<T> identifier) {
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
