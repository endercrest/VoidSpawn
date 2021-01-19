package com.endercrest.voidspawn.modes.flags;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.Sound;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class SoundFlag extends BaseFlag<Sound> {
    private static final List<String> sounds = Collections.unmodifiableList(Arrays.stream(Sound.values()).map(Sound::name).collect(Collectors.toList()));

    public SoundFlag(FlagIdentifier<Sound> identifier) {
        super(identifier);
    }

    @Override
    public Optional<Sound> getValue(World world) {
        String value = ConfigManager.getInstance().getFlag(world.getName(), getIdentifier());
        if (value == null)
            return Optional.empty();

        try {
            Sound sound = Sound.valueOf(value.toUpperCase());
            return Optional.of(sound);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public void setValue(@NotNull World world, String value) {
        try {
            Sound.valueOf(value);
            super.setValue(world, value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(value + " is not a valid sound!");
        }
    }

    @Override
    public void setValue(@NotNull World world, String[] args) throws IllegalArgumentException {
        setValue(world, String.join(" ", args));
    }

    @Override
    public List<String> getOptions() {
        return sounds;
    }
}
