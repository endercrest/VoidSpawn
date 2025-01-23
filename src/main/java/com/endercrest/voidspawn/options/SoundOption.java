package com.endercrest.voidspawn.options;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class SoundOption extends BaseOption<String> {

    public SoundOption(OptionIdentifier<String> identifier) {
        super(identifier);
    }

    @Override
    public Optional<String> getLoadedValue(@NotNull World world) {
        String value = ConfigManager.getInstance().getOption(world.getName(), getIdentifier());

        return Optional.ofNullable(value);
    }

    @Override
    public void setValue(@NotNull World world, String value) {
        super.setValue(world, value);
    }

    @Override
    public void setValue(@NotNull World world, String[] args) throws IllegalArgumentException {
        setValue(world, String.join(" ", args));
    }

    @Override
    public List<String> getOptions() {
        List<String> options = Registry.SOUNDS.stream()
                .map(Sound::getKeyOrNull)
                .filter(Objects::nonNull)
                .map(NamespacedKey::toString)
                .collect(Collectors.toCollection(ArrayList::new));
        options.addFirst("<custom sounds supported, format: [namespace]:[sound]>");

        return options;
    }
}
